package ca.mbjolin.editor.preprocess._2_macro.grammar;

import org.antlr.v4.runtime.ParserRuleContext;

import ca.mbjolin.editor.preprocess._2_macro.exception.MacroException;

public class ScopeSymbol extends MacroSymbol {

  Boolean isBegin;

  public  <T extends ParserRuleContext>  ScopeSymbol(T ctx, Boolean isBegin) {
    type = MacroSymbolType.SCOPE;
    this.setIdObject(ctx.hashCode());
    fillParent(ctx);
    this.isBegin = isBegin;
  }

  public MacroException verification() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'verification'");
  }

  public String render() {
    return "";
  }
}
