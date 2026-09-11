package ca.mbjolin.editor.evaluation;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;
import org.testcontainers.containers.Container.ExecResult;
import org.testcontainers.junit.jupiter.Testcontainers;

import ca.mbjolin.editor.journal.Message;

/*
 * CA02 : Vérifier que l’IAR repose que sur du formalisme.
 * 
 * Utilise les descriptions formelles suivantes :
 * epure_1 : AsciidocMacroParser.g4, AsciidocMacroLexer.g4;
 * formel_1 : AsciidocParser.g4, AsciidocLexer.g4, CustomRule.yml;
 * presente_1 : PresenteModelisateur.stg.
 */
@Testcontainers
public class Cas02Test extends SimulateUserInterfaceInteraction {

  @Test
  public void executeSessionTest() throws Exception {
    // GIVEN
    InputStream is = getClass().getClassLoader()
        .getResourceAsStream("cas02/description_1.txt");
    String description_1 = new String(is.readAllBytes(), StandardCharsets.UTF_8);

    /* Prend formel_1, epure_1, presente_1 */
    content = loadDefaultContent("cas02/session.json");

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

    /*
     * Un modèle vérifié contenu dans la base de données est associé à des règles et
     * des langages.
     */

    /* Relation entre le modèle et la session qui contient les règles et les langages à appliquer. */
    ExecResult languageAndRuleRelation =
        database.execInContainer("psql", "-U", "default-user", "-d", "editor-ldm", "-c",
            "SELECT * from \"document_pri\".document as doc inner join \"concevoir_pri\".simplesession as ses on doc.id_projet = ses.titre ;");
    
    Assertions.assertTrue(languageAndRuleRelation.getStdout().contains("projectDefaultTitle"));
    /* Relations sur les concepts dans une version ultérieure. */

    /* La journalisation affiche la conformité des informations provenant de documents, de
     * descriptions proposée d’un modèle ou d’une représentation standardisée du modèle par rapport
     * à leurs métamodèles.
     */
    Assertions.assertFalse(content.getSystemJ().getMessages().isEmpty());

    /*
     * La journalisation affiche les transformations ainsi que les formalismes utilisés par elles.
     */
    Assertions.assertTrue(content.getSystemJ().getMessagesAsString().contains("\"etape\": \"epure\""));
    Assertions.assertTrue(content.getSystemJ().getMessagesAsString().contains("\"transformation\": \"Asciidoc\""));
    
    /* La journalisation affiche les intrants aux transformations. */
    Assertions.assertTrue(content.getSystemJ().getMessagesAsString().contains("\"intrant\": \"epure1\""));
    Assertions.assertTrue(content.getSystemJ().getMessagesAsString().contains("\"extrant\": \"mmd\""));
    
    /*
     * La journalisation affiche les différents vérifications avec leurs intrants (des modèles, des
     * configurations, des documents) puis leurs niveaux (lexical, syntaxique et sémantique).
     */
    Assertions.assertTrue(content.getSystemJ().getMessagesAsString().contains("INFO - Début de la traduction."));
    Assertions.assertTrue(content.getSystemJ().getMessagesAsString().contains("TRACE - Transformation (identifiant):Asciidoc"));
    Assertions.assertTrue(content.getSystemJ().getMessagesAsString().contains("TRACE - Extrant 1 (identifiant):= DocumentModele_v1"));

  }

}
