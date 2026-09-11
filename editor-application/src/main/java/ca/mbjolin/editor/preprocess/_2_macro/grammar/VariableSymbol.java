package ca.mbjolin.editor.preprocess._2_macro.grammar;

import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;

import ca.mbjolin.editor.preprocess._2_macro.exception.MacroException;

public class VariableSymbol extends MacroSymbol {

  String content;

  String value;

  String key;

  public <T extends ParserRuleContext> VariableSymbol(T ctx, String key, String value) {
    type = MacroSymbolType.VARIABLE;
    this.setIdObject(ctx.hashCode());
    fillParent(ctx);
    content = ctx.getText();
    this.key = key;
    this.value = value;
  }

  public MacroException verification(Map<String, String> variables) {
    MacroException exception = null;
    if (variables.containsKey(key)) {
      exception = new MacroException(this, "La variable existe déjà.");
    }
    return exception;
  }

  public String render() {
    return content;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}
