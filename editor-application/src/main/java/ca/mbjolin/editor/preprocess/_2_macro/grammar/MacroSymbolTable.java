package ca.mbjolin.editor.preprocess._2_macro.grammar;

import java.util.ArrayList;
import java.util.List;

/* MBJ 2025-02-14 : 
 * Il n'y a pas de Map dans la table de symboles puisqu'on ne peut pas gérer le concept 
 * de Scope. Les Macros est un raccourci pour remplacer des morceaux de code et va considérer la
 * résolution des macros précédentes. Exemple : Une macro include qui contient une macro 
 * substitution qui a besoin d'une macro variable se trouvant dans un include précédent.
 * */
public class MacroSymbolTable {

  private List<MacroSymbol> table = new ArrayList<>();

  private List<MacroSymbolType> resolutionOrder = new ArrayList<MacroSymbolType>();

  public MacroSymbolTable(List<MacroSymbolType> resolutionOrder) {
    this.resolutionOrder.addAll(resolutionOrder);
  }

  public void add(MacroSymbol symbol) {
    table.add(symbol);
  }

  public List<MacroSymbol> getTable() {
    return table;
  }

  public List<MacroSymbolType> getResolutionOrder() {
    return resolutionOrder;
  }

  public MacroSymbol resolve(Integer symbolId) {
    return table.get(symbolId);
  }

}
