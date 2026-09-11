package ca.mbjolin.editor.evaluation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;
import org.testcontainers.junit.jupiter.Testcontainers;

import ca.mbjolin.editor.journal.Message;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Document;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Paragraphe;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Reglelogique;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Section;

/*
 * CA01 : Vérifier que l’IAR permet la création ou la modification d’un modèle à partir
 * d’une description proposée. Ainsi que la journalisation du système, l’épuration de la
 * description proposée et la présentation du modèle.
 * 
 * Utilise les descriptions formelles suivantes :
 * formel_1 : AsciidocParser.g4, AsciidocLexer.g4, CustomRule.yml;
 * epure_1 : AsciidocMacroParser.g4, AsciidocMacroLexer.g4;
 * presente_1 : PresenteModelisateur.stg.
 */
@Testcontainers
public class Cas01Test extends SimulateUserInterfaceInteraction {

  @Test
  public void successExecuteSessionTest() throws IOException {
    // GIVEN
    InputStream is = getClass().getClassLoader()
        .getResourceAsStream("cas01/description_1.txt");
    String description_1 = new String(is.readAllBytes(), StandardCharsets.UTF_8);
    InputStream is2 = getClass().getClassLoader()
        .getResourceAsStream("cas01/description_2.txt");
    String description_2 = new String(is2.readAllBytes(), StandardCharsets.UTF_8);

    /*Prend formel_1, epure_1, presente_1*/
    content = loadDefaultContent("cas01/session.json");

    // WHEN
    content.setDescription1(description_1);
    content = sessionService.execute(content);
    Document old = documentModelService.save(content.getAtteste());
    content.setDescription1(description_2);
    content = sessionService.execute(content);
    Document docInDb = documentModelService.update(content.getAtteste(), old);

    ArrayList<String> sectionEtiquette = new ArrayList<String> ();
    for(Section section : docInDb.sections()) {
      sectionEtiquette.add(section.etiquette().value());
    }
    
    ArrayList<Reglelogique> reglelogiques = new ArrayList<Reglelogique>();
    for(Section section : docInDb.sections()) {
      for(Paragraphe paragraphe : section.paragraphes())
        reglelogiques.addAll(paragraphe.reglelogiquex());
    }

    // THEN
    System.out.println(content.getSystemJ().getMessagesAsString());
    System.out.println(content.getPresente().toString());
    
    /* Vérification diligente du déroulement. */
    for(Message message : content.getSystemJ().getMessages()) {
      Assertions.assertFalse(message.getLevel().equals(Level.ERROR));
    }

    /* Le système est en mesure de fournir un journal sur le processus et son déroulement. */
    Assertions.assertFalse(content.getSystemJ().getMessages().isEmpty());
    
    /* Les informations sélectionnées comme superflus ont été épurées. */
    Assertions.assertFalse(content.getEpure1().contains("ifdef"));

    /* La base de données (Modèle vérifié) contient les informations provenant de description_1 
     * dont la description_2 ne les modifie pas. */
    Assertions.assertTrue(sectionEtiquette.contains("=== SectionAa"));

    /* La base de données (Modèle vérifié) contient les informations provenant de description_2 
     * changeant des informations de la description_1. */
    Assertions.assertTrue(sectionEtiquette.contains("== SectionA"));

    /* La base de données (Modèle vérifié) contient les informations provenant de description_2 
     * ne changeant  pas des informations de la description_1. */
    Assertions.assertTrue(sectionEtiquette.contains("== SectionB"));

    /* Le système est en mesure de présenter le modèle développé depuis description_1 et 
     * description_2. */
    Assertions.assertFalse(content.getPresente().isEmpty());
    Assertions.assertFalse(reglelogiques.isEmpty());
    String ruleString = "";
    for(Reglelogique rule : reglelogiques) {
      ruleString = ruleString.concat(rule.getDescriptionN());
    }
    Assertions.assertTrue(ruleString.contains("RegleTest=bien"));
  }
  
  @Test
  public void failExecuteSessionTest() throws IOException {
    // GIVEN
    InputStream is = getClass().getClassLoader()
        .getResourceAsStream("cas01/description_1.txt");
    String description_1 = new String(is.readAllBytes(), StandardCharsets.UTF_8);
    InputStream is2 = getClass().getClassLoader()
        .getResourceAsStream("cas01/description_2_witherror.txt");
    String description_2 = new String(is2.readAllBytes(), StandardCharsets.UTF_8);

    /*Prend formel_1, epure_1, presente_1*/
    content = loadDefaultContent("cas01/session.json");

    // WHEN
    content.setDescription1(description_1);
    content = sessionService.execute(content);
    Document old = documentModelService.save(content.getAtteste());
    content.setDescription1(description_2);
    content = sessionService.execute(content);
    Document docInDb = documentModelService.update(content.getAtteste(), old);

    ArrayList<String> sectionEtiquette = new ArrayList<String> ();
    for(Section section : docInDb.sections()) {
      sectionEtiquette.add(section.etiquette().value());
    }
    
    ArrayList<Reglelogique> reglelogiques = new ArrayList<Reglelogique> ();
    for(Section section : docInDb.sections()) {
      for(Paragraphe paragraphe : section.paragraphes())
        reglelogiques.addAll(paragraphe.reglelogiquex());
    }

    // THEN
    System.out.println(content.getSystemJ().getMessagesAsString());
    System.out.println(content.getPresente().toString());
    
    /* Vérification diligente du déroulement. */
    for(Message message : content.getSystemJ().getMessages()) {
      Assertions.assertFalse(message.getLevel().equals(Level.ERROR));
    }

    /* Le système est en mesure de fournir un journal sur le processus et son déroulement. */
    Assertions.assertFalse(content.getSystemJ().getMessages().isEmpty());
    
    /* Les informations sélectionnées comme superflus ont été épurées. */
    Assertions.assertFalse(content.getEpure1().contains("ifdef"));

    /* La base de données (Modèle vérifié) contient les informations provenant de description_1 
     * dont la description_2 ne les modifie pas. */
    Assertions.assertTrue(sectionEtiquette.contains("=== SectionAa"));

    /* La base de données (Modèle vérifié) contient les informations provenant de description_2 
     * changeant des informations de la description_1. */
    Assertions.assertTrue(sectionEtiquette.contains("== SectionA"));

    /* La base de données (Modèle vérifié) contient les informations provenant de description_2 
     * ne changeant  pas des informations de la description_1. */
    Assertions.assertTrue(sectionEtiquette.contains("== SectionB"));

    /* Le système est en mesure de présenter le modèle développé depuis description_1 et 
     * description_2. */
    Assertions.assertFalse(content.getPresente().isEmpty());
    Assertions.assertFalse(reglelogiques.isEmpty());
    String ruleString = "";
    for(Reglelogique rule : reglelogiques) {
      ruleString = ruleString.concat(rule.getDescriptionN());
    }
    Assertions.assertFalse(ruleString.contains("RegleTest=bien"));
  }

}
