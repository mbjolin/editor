package ca.mbjolin.editor.integration;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.mbjolin.editor.config.AppConfig;
import ca.mbjolin.editor.facade.AntlrFacade;
import ca.mbjolin.editor.util.CustomClassLoader;

public class GenerateLanguageTest {

  static AntlrFacade antlrFacade;

  @BeforeAll
  public static void setup() {
    AppConfig config = new AppConfig();
    CustomClassLoader custom = new CustomClassLoader(config);
    antlrFacade = new AntlrFacade(config, custom);
  }

  @Test
  public void execute() throws Exception {

    antlrFacade.generateJava("AsciidocMacro", "ca.mbjolin.antlr.asciidocmacro");
    antlrFacade.generateJava("Asciidoc", "ca.mbjolin.antlr.asciidoc");
    antlrFacade.generateJava("ReStructuredText", "ca.mbjolin.antlr.restructuredtext");
    antlrFacade.generateJava("Document", "ca.mbjolin.antlr.document");
    //antlrFacade.generateJava("Rule", "ca.mbjolin.antlr.rule");
  }

}
