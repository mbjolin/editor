package ca.mbjolin.editor.preprocess._2_macro.grammar;

import org.antlr.v4.runtime.ParserRuleContext;

import ca.mbjolin.editor.preprocess._2_macro.exception.MacroException;

public class ContentSymbol extends MacroSymbol {

  String content;

  public  <T extends ParserRuleContext>  ContentSymbol(T ctx) {
    type = MacroSymbolType.CONTENT;
    this.setIdObject(ctx.hashCode());
    fillParent(ctx);
    content = ctx.getText();
  }
  
  public ContentSymbol(String msg) {
    type = MacroSymbolType.CONTENT;
    content = msg;
  }

  public MacroException verification() {
    return null;
  }

  public String render() {
    return content;
  }
  
}
