package ca.mbjolin.editor.preprocess._2_macro.grammar;

import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;

import ca.mbjolin.editor.preprocess._2_macro.exception.MacroException;

public class SubstitutionSymbol extends MacroSymbol {

  String content;

  String key;

  public <T extends ParserRuleContext> SubstitutionSymbol(T ctx, String key) {
    type = MacroSymbolType.SUBSTITUTION;
    this.setIdObject(ctx.hashCode());
    fillParent(ctx);
    content = ctx.getText();
    this.key = key;
  }

  public MacroException verification(Map<String, String> variables) {
    MacroException exception = null;
    if (!variables.containsKey(key)) {
      exception =
          new MacroException(this, "Impossible de faire la substitution la variable n'existe pas.");
    }
    return exception;
  }

  public String render(Map<String, String> variables) {
    return variables.get(key);
  }

}
