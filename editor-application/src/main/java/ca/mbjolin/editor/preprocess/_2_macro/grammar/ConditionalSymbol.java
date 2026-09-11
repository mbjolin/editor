package ca.mbjolin.editor.preprocess._2_macro.grammar;

import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;

import ca.mbjolin.editor.preprocess._2_macro.exception.MacroException;

public class ConditionalSymbol extends MacroSymbol {

  String content;

  String key;

  String value;

  public <T extends ParserRuleContext> ConditionalSymbol(T ctx, String key, String value) {
    type = MacroSymbolType.CONDITIONAL;
    this.setIdObject(ctx.hashCode());
    fillParent(ctx);
    content = ctx.getText();
    this.key = key;
    this.value = value;
  }

  public MacroException verification(Map<String, String> variables) {
    MacroException exception = null;
    if (!variables.containsKey(key)) {
      exception =
          new MacroException(this,
              "Impossible de résoudre la conditionnel puisque la variable n'existe pas.");
    }

    return exception;
  }

  public String render(Map<String, String> variables) {
    String result = "";
    String conditionString = variables.get(key);
    if (!(conditionString.isBlank() | conditionString.isEmpty())) {
      result = value;
    }
    return result;
  }


}
