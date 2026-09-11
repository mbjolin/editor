package ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro;

import org.antlr.v4.runtime.ParserRuleContext;

public class Text extends ParserRuleContext {
  public String get() {
    String value = null;
    if(!isEmpty()) {
      value = getText();
    }
    return value;
  }
}
