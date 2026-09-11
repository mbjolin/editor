package ca.mbjolin.editor.evaluation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;
import org.testcontainers.junit.jupiter.Testcontainers;

import ca.mbjolin.editor.journal.Message;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Document;

/*
 * CA05 : Vérifier que l’IAR permet de maintenir un modèle.
 * 
 * Utilise les descriptions formelles suivantes :
 * formel_1 : AsciidocParser.g4, AsciidocLexer.g4, CustomRule.yml;
 * epure_1 : AsciidocMacroParser.g4, AsciidocMacroLexer.g4;
 * presente_1 : PresenteModelisateur.stg.
 */
@Testcontainers
public class Cas06Test extends SimulateUserInterfaceInteraction {

  @Test
  public void executeTest() throws IOException {
    // GIVEN
    InputStream is = getClass().getClassLoader()
        .getResourceAsStream("cas06/description_1.txt");
    String description_1 = new String(is.readAllBytes(), StandardCharsets.UTF_8);
   
    /* Prend formel_1, epure_1, presente_1 */
    content = loadDefaultContent("cas06/session.json");
    content.setDescription1(description_1);
    content = sessionService.execute(content);
    Document old = documentModelService.save(content.getAtteste());
    /* Vérification diligente du déroulement. */
    for (Message message : content.getSystemJ().getMessages()) {
      Assertions.assertFalse(message.getLevel().equals(Level.ERROR));
    }

    // THEN
    System.out.println(content.getSystemJ().getMessagesAsString());
  }

}
