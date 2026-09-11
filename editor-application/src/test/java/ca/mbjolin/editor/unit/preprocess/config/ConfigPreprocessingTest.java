package ca.mbjolin.editor.unit.preprocess.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.preprocess._1_config.ConfigPreprocessing;

public class ConfigPreprocessingTest {

  static private ConfigPreprocessing configPreprocessing;

  @BeforeAll
  public static void setup() {
    configPreprocessing = new ConfigPreprocessing(new Journal());
  }

  @Test
  public void executeBaseTest() {
    configPreprocessing.execute(config1());

    Assertions.assertTrue(configPreprocessing.getJournal().isWithoutError(),
        configPreprocessing.getJournal().getMessagesAsString());
  }

  public String config1() {
    String configString =
        """
            épuration;adoc;AsciidocMacro
            formalisation;adoc;Asciidoc
            présentation;*;ConceptionPresentation
            """;
    return configString;
  }
}
