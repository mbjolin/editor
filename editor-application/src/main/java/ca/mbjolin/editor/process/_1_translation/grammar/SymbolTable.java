package ca.mbjolin.editor.process._1_translation.grammar;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;

public class SymbolTable {

  private Map<Integer, Symbol> tables = new HashMap<>();

  public SymbolTable() {}

  public void add(ParserRuleContext ctx, Symbol symbol) {
    tables.put(ctx.hashCode(), symbol);
  }

  public Map<Integer, Symbol> getTable() {
    return tables;
  }

  public Symbol resolve(Integer symbolId) {
    return tables.get(symbolId);
  }

}
