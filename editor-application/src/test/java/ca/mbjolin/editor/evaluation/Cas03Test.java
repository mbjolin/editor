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
 * CA03 : Vérifier que l’utilisateur peut contextualiser l’IAR.
 * 
 * Utilise les descriptions formelles suivantes :
 * epure_1 : AsciidocMacroParser.g4, AsciidocMacroLexer.g4;
 * formel_1 : AsciidocParser.g4, AsciidocLexer.g4, CustomRule.yml;
 * presente_1 : PresenteApprobateur.stg.
 */
@Testcontainers
public class Cas03Test extends SimulateUserInterfaceInteraction {


  @Test
  public void executeSessionTest() throws IOException {
    // GIVEN
    InputStream is = getClass().getClassLoader()
        .getResourceAsStream("cas03/description_1.txt");
    String description_1 = new String(is.readAllBytes(), StandardCharsets.UTF_8);

    /* Prend formel_1, epure_1, presente_1 */
    content = loadDefaultContent("cas03/session.json");

    // WHEN
    content.setDescription1(description_1);
    content = sessionService.execute(content);
    documentModelService.save(content.getAtteste());
    sessionService.save(content);

    // THEN
    System.out.println(content.getSystemJ().getMessagesAsString());
    System.out.println(content.getPresente().toString());

    /* Vérification diligente du déroulement. */
    for (Message message : content.getSystemJ().getMessages()) {
      Assertions.assertFalse(message.getLevel().equals(Level.ERROR));
    }

    Assertions.assertTrue(content.getPresente().lines().count() == 8);
  }

}
