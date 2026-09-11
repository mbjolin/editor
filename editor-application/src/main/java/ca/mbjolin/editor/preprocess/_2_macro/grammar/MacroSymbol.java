package ca.mbjolin.editor.preprocess._2_macro.grammar;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.apache.commons.lang3.tuple.Pair;

public abstract class MacroSymbol {

  protected MacroSymbolType type;
  private Integer idObject;
  private List<Integer> parents = new ArrayList<>();
  private Pair<Integer, Integer> start;
  private Pair<Integer, Integer> stop;

  public List<Integer> getParents() {
    return parents;
  }

  public void fillMacro(ParserRuleContext ctx) {
    ctx.hashCode();
  }

  public void fillParent(ParserRuleContext ctx) {
    ParserRuleContext parent = ctx.getParent();
    if (parent != null) {
      parents.add(parent.hashCode());
      addParent(parent, 1);
    }
    if (ctx.getStart() != null) {
      start = Pair.of(ctx.getStart().getLine(), ctx.getStart().getCharPositionInLine());
    } else {
      stop = null;
    }
    if (ctx.getStop() != null) {
      stop = Pair.of(ctx.getStop().getLine(), ctx.getStop().getCharPositionInLine());
    } else {
      stop = null;
    }
  }

  public void addParent(ParserRuleContext ctx, Integer depth) {
    depth = depth + 1;
    ParserRuleContext parent = ctx.getParent();
    if (parent != null && depth < 10) {
      parents.add(parent.hashCode());
      addParent(parent, depth);
    }
  }

  public Integer getIdObject() {
    return idObject;
  }

  public void setIdObject(Integer idObject) {
    this.idObject = idObject;
  }

  public MacroSymbolType getType() {
    return type;
  }

  public String getPosition() {
    return start.toString() + stop.toString();
  }
}
