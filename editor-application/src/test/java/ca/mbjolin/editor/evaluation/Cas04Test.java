package ca.mbjolin.editor.evaluation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;
import org.testcontainers.junit.jupiter.Testcontainers;

import ca.mbjolin.editor.journal.Message;


/*
 * CA04 : Vérifier que l’IAR permet de former un seul modèle à partir de plusieurs 
 * documents provenant de différents contextes.
 * 
 * Utilise les descriptions formelles suivantes :
 * formel_1 : AsciidocParser.g4, AsciidocLexer.g4, CustomRule.yml, AlloyEngine;
 * epure_1 : AsciidocMacroParser.g4, AsciidocMacroLexer.g4;
 * presente_1 : PresenteModelisateur.stg.
 * 
 * Note : L'ordre est important lors de la résolution dans Alloy, ce qui ne devrait pas être 
 * important dans un système formel. J'ai dû retirer une partie de l'exemple.
 */
@Testcontainers
public class Cas04Test extends SimulateUserInterfaceInteraction {


  @Test
  public void executeSessionTest() throws IOException {
    // GIVEN
    InputStream is = getClass().getClassLoader()
        .getResourceAsStream("cas04/description_1.txt");
    String description_1 = new String(is.readAllBytes(), StandardCharsets.UTF_8);
    InputStream is2 = getClass().getClassLoader()
        .getResourceAsStream("cas04/description_2.txt");
    String description_2 = new String(is2.readAllBytes(), StandardCharsets.UTF_8);

    /* Prend formel_1, epure_1, presente_1 */
    content = loadDefaultContent("cas04/session.json");

    // WHEN
    content.setDescription1(description_1);
    content.setDescription2(description_2);
    content = sessionService.execute(content);
    documentModelService.save(content.getAtteste());

    // THEN
    System.out.println(content.getSystemJ().getMessagesAsString());

    for (Message message : content.getSystemJ().getMessages()) {
      Assertions.assertFalse(message.getLevel().equals(Level.ERROR));
    }
  }

  @Test
  public void executeSessionMissingPartTest() throws IOException {
    // GIVEN
    InputStream is = getClass().getClassLoader()
        .getResourceAsStream("cas04/description_1.txt");
    String description_1 = new String(is.readAllBytes(), StandardCharsets.UTF_8);
    InputStream is2 = getClass().getClassLoader()
        .getResourceAsStream("cas04/descriptionempty_2.txt");
    String description_2 = new String(is2.readAllBytes(), StandardCharsets.UTF_8);

    /* Prend formel_1, epure_1, presente_1 */
    content = loadDefaultContent("cas04/session.json");

    // WHEN
    content.setDescription1(description_1);
    content.setDescription2(description_2);
    content = sessionService.execute(content);
    documentModelService.save(content.getAtteste());

    // THEN
    System.out.println(content.getSystemJ().getMessagesAsString());
    Assertions.assertTrue(content.getSystemJ().getMessagesAsString().contains("Syntax error in"));
  }
}
