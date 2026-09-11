package ca.mbjolin.editor.unit.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.mbjolin.editor.config.AppConfig;
import ca.mbjolin.editor.facade.AntlrFacade;
import ca.mbjolin.editor.util.CustomClassLoader;

@Disabled
public class AntlrServiceTest {

  static AntlrFacade antlrService;

  @BeforeAll
  public static void setup() {
    AppConfig config = new AppConfig();
    CustomClassLoader custom = new CustomClassLoader(config);
    antlrService = new AntlrFacade(config, custom);
  }

  @Test
  public void generateGrammar() throws Exception {
    antlrService.generateJava("Document", "ca.mbjolin.antlr.document");
    //service.generateCode("Alloy", "ca.mbjolin.antlr.alloy");
    antlrService.generateJava("Asciidoc", "ca.mbjolin.antlr.asciidoc");
    antlrService.generateJava("AsciidocTest", "ca.mbjolin.antlr.asciidoctest");
    antlrService.generateJava("AsciidocSymbol", "ca.mbjolin.antlr.asciidocsymbol");
    //antlrService.generateJava("Example", "ca.mbjolin.antlr.example");
    //service.generateCode("JSON", "ca.mbjolin.antlr.json");
    //antlrService.generateJava("Gabarit", "ca.mbjolin.antlr.gabarit");

  }

  @Disabled
  //@Test
  public void documentGrammarTest() throws Exception {
    antlrService.showTree("Document", "document-example3.editor");
    //antlrService.showTree("Document", "document-example2.editor");
    //service.listenerResult("Editor", "editor-example1.editor");
  }

  @Disabled
  @Test
  public void alloyGrammarTest() throws Exception {
    antlrService.showTree("Alloy", "alloy-example1.alloy");
    antlrService.showTree("Alloy", "alloy-example2.alloy");
    antlrService.showTree("Alloy", "alloy-example3.alloy");
    antlrService.showTree("Alloy", "alloy-example4.alloy");
    //service.listenerResult("Alloy", "alloy-example1.alloy");
  }

  @Test
  public void asciidocGrammarTest() throws Exception {
    //antlrService.showTree("Asciidoc", "asciidoc-example1.adoc");
    //antlrService.showTree("AsciidocTest", "asciidoc-example1.adoc");
    antlrService.showTree("AsciidocSymbol", "asciidoc-example1.adoc");
    //antlrService.showTree("Asciidoc", "asciidoc-example2.adoc");
    //antlrService.showTree("Asciidoc", "asciidoc-example3.adoc");
    //antlrService.showTree("Asciidoc", "asciidoc-example4.adoc");
    //service.listenerResult("Asciidoc", "asciidoc-example1.adoc");
  }

  @Disabled
  @Test
  public void exampleGrammarTest() throws Exception {
    antlrService.showTree("Example", "example1.ex");
    //antlrService.showTree("Example", "example2.ex");
    //antlrService.showTree("Example", "example3.ex");
    //antlrService.showTree("Example", "example4.ex");
    antlrService.listenerResult("Example", "example1.ex");
  }

  @Disabled
  @Test
  public void jsonGrammarTest() throws Exception {
    antlrService.showTree("JSON", "json-example1.json");
    antlrService.showTree("JSON", "json-example2.json");
    antlrService.showTree("JSON", "json-example3.json");
    antlrService.showTree("JSON", "json-example4.json");
    //service.listenerResult("JSON", "json-example1.json");
  }

  @Test
  @Disabled
  public void gabaritGrammarTest() throws Exception {
    antlrService.showTree("Gabarit", "gabarit-exemple1.gab");
    //antlrService.showTree("Example", "example2.ex");
    //antlrService.showTree("Example", "example3.ex");
    //antlrService.showTree("Example", "example4.ex");
    antlrService.listenerResult("Gabarit", "gabarit-exemple1.gab");
  }

}
