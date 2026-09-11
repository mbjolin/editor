package ca.mbjolin.editor.process._1_translation.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;

public class Symbol {

  private Integer idObject;
  private SymbolType type;
  private List<Integer> parents = new ArrayList<>();

  private Map<SymbolAttribut, List<String>> attributs = new HashMap<>();

  public <T extends ParserRuleContext> Symbol(SymbolType type, T ctx) {
    this.type = type;
    this.setIdObject(ctx.hashCode());
    fillParent(ctx);
  }

  public List<Integer> getParents() {
    return parents;
  }

  public void fillParent(ParserRuleContext ctx) {
    ParserRuleContext parent = ctx.getParent();
    if(parent != null) {
      parents.add(parent.hashCode());
      addParent(parent, 1);
    }
  }

  public void addParent(ParserRuleContext ctx, Integer depth) {
    depth = depth + 1;
    ParserRuleContext parent = ctx.getParent();
    if(parent != null && depth < 10) {
      parents.add(parent.hashCode());
      addParent(parent, depth);
    }
  }

  public <T extends ParserRuleContext>  void addAttribut(SymbolAttribut attribut, T value) {
    if(value != null) {
      if(!attributs.containsKey(attribut)) {
        attributs.put(attribut, new ArrayList<>());
      }
      attributs.get(attribut).add(value.getText());
    }
  }

  public void addAttribut(SymbolAttribut attribut, String value) {
    if(value != null) {
      if(!attributs.containsKey(attribut)) {
        attributs.put(attribut, new ArrayList<>());
      }
      attributs.get(attribut).add(value);
    }
  }

  public String getAttribut(SymbolAttribut attribut) {
    String attributValue = null;
    if(attributs.containsKey(attribut)) {
      attributValue = attributs.get(attribut).get(0);
    }
    return attributValue;
  }

  public SymbolType getType() {
    return type;
  }

  public Integer getIdObject() {
    return idObject;

  }

  public void setIdObject(Integer idObject) {
    this.idObject = idObject;

  }

}
