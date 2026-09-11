package ca.mbjolin.editor.unit.process._2_intermediaterepresentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.mbjolin.editor.config.AppConfig;
import ca.mbjolin.editor.facade.AntlrFacade;
import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.process._2_intermediaterepresentation.RuleProcessing;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Contenu;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Document;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Meta;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Paragraphe;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Section;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Description;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Etiquette;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Identifiant;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Valeur;
import ca.mbjolin.editor.util.CustomClassLoader;

@Disabled("Début d'une grammaire pour exprimer les règles, non terminée.")
public class RuleProcessingTest {

  static private AntlrFacade antlrFacade;

  @BeforeAll
  public static void setup() {
    AppConfig config = new AppConfig();
    CustomClassLoader custom = new CustomClassLoader(config);
    antlrFacade = new AntlrFacade(config, custom);
  }

  @Test
  public void executeBaseTest() throws IOException, InterruptedException {
    // GIVEN
    antlrFacade.generateJava("Rule", "ca.mbjolin.antlr.rule");
    RuleProcessing rule = new RuleProcessing(new Journal());

    // WHEN
    rule.execute(docEx1(), ruleEx1());

    // THEN
    Journal jou = rule.getJournal();
    Assertions.assertTrue(jou.isWithoutError(), jou.getMessagesAsString());

    Document doc = rule.getDocumentOut();
    Assertions.assertTrue(doc.metas().size() == 2);
    Assertions.assertTrue(doc.sections().size() == 1);
  }

  public Document docEx1() {
    Document doc = new Document(new Identifiant("id"), new Identifiant("projetId"),
        new ArrayList<>(), new ArrayList<>());
    doc.metas().add(
        new Meta(new Identifiant("meta1"), new Etiquette("etiquette1"), new Valeur("valeur1")));
    doc.metas().add(
        new Meta(new Identifiant("meta2"), new Etiquette("etiquette2"), new Valeur("valeur2")));

    List<Contenu> contenus = new ArrayList<>();
    contenus.add(new Contenu(new Identifiant("id"), new Description("description")));
    List<Paragraphe> paras = new ArrayList<>();
    paras.add(new Paragraphe(new Identifiant("para1"), contenus,
        new ArrayList<>(), new ArrayList<>()));
    doc.sections().add(
        new Section(new Identifiant("section1"), new Etiquette("etiquette1"), paras, null, true));
    return doc;
  }

  //
  //

  // document.idenfiant devient genere aleatoire
//@formatter:off
  public String ruleEx1() {
    return 
"""
doc (document.identifiant) est-vide
doc (document.identifiant) est-vide donc doc (document.identifiant, 'nouveauID') devient 
""";
  }
//@formatter:on

}
