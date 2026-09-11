package ca.mbjolin.editor.service;

import ca.mbjolin.editor.facade.AntlrFacade;
import ca.mbjolin.editor.facade.JavaFacade;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class GrammarService {

  private AntlrFacade antlrService;

  private JavaFacade javaService;

  @Inject
  public GrammarService(AntlrFacade antlrService, JavaFacade javaService) {
    this.antlrService = antlrService;
    this.javaService = javaService;
  }

  public void generateAndIncludeGrammar(String grammar) throws Exception {
    antlrService.generateJava(grammar, "ca.mbjolin.antlr." + grammar.toLowerCase());
    javaService.compileJava(grammar, "ca.mbjolin.antlr." + grammar.toLowerCase());
    //javaService.verifyIncludeClass("ca.mbjolin.antlr." + grammar.toLowerCase());
    antlrService.showTree("Example", "example1.ex");
  }

}
