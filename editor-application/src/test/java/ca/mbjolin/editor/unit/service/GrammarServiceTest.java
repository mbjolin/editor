package ca.mbjolin.editor.unit.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.mbjolin.editor.config.AppConfig;
import ca.mbjolin.editor.facade.AntlrFacade;
import ca.mbjolin.editor.facade.JavaFacade;
import ca.mbjolin.editor.service.GrammarService;
import ca.mbjolin.editor.util.CustomClassLoader;

public class GrammarServiceTest {

  static GrammarService grammarService;

  @BeforeAll
  public static void setup() {
    AppConfig config = new AppConfig();
    CustomClassLoader custom = new CustomClassLoader(config);
    AntlrFacade antlrService = new AntlrFacade(config, custom);
    JavaFacade javaService = new JavaFacade(config, custom);
    grammarService = new GrammarService(antlrService, javaService);
  }

  @Test
  public void generateAndIncludeGrammarTest() throws Exception {
    // grammarService.generateAndIncludeGrammar("Editor");
    //grammarService.generateAndIncludeGrammar("JSON");
    //grammarService.generateAndIncludeGrammar("Example");
  }

}
