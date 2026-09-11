package ca.mbjolin.editor.preprocess._2_macro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.event.Level;

import ca.mbjolin.editor.facade.AntlrFacade;
import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.preprocess._2_macro.exception.MacroException;
import ca.mbjolin.editor.preprocess._2_macro.grammar.ConditionalSymbol;
import ca.mbjolin.editor.preprocess._2_macro.grammar.ContentSymbol;
import ca.mbjolin.editor.preprocess._2_macro.grammar.IncludeSymbol;
import ca.mbjolin.editor.preprocess._2_macro.grammar.MacroSymbol;
import ca.mbjolin.editor.preprocess._2_macro.grammar.MacroSymbolTable;
import ca.mbjolin.editor.preprocess._2_macro.grammar.MacroSymbolType;
import ca.mbjolin.editor.preprocess._2_macro.grammar.SubstitutionSymbol;
import ca.mbjolin.editor.preprocess._2_macro.grammar.VariableSymbol;
import ca.mbjolin.editor.util.LoggingErrorListener;

public class MacroPreprocessing {

  private AntlrFacade antlrFacade;
  private String grammarName;
  private String in;
  private List<String> lexicalGrammarErrors = new ArrayList<String>();
  private MacroSymbolTable macroSymbolTable;
  private String out;
  private List<MacroException> symbolErrors = new ArrayList<MacroException>();
  private List<String> syntaxGrammarErrors = new ArrayList<String>();
  private List<String> result = new ArrayList<String>();
  private Map<String, String> variables = new HashMap<>();
  private Map<String, String> includes = new HashMap<>();

  private Journal journal;

  public MacroPreprocessing(AntlrFacade antlrFacade, String transformation, Journal journal) {
    this.antlrFacade = antlrFacade;
    this.grammarName = transformation;
    this.journal = journal;
  }

  public void addInclude(Map<String, String> includes) {
    this.includes = includes;
  }

  public void execute(String content) {
    in = content;
    macroSymbolTable = compileString(content, true);
    toIntermediateCode();
  }

  private MacroSymbolTable compileString(String content, Boolean isErrorPreserved) {
    LoggingErrorListener lexicalAnalysisError = new LoggingErrorListener();
    LoggingErrorListener syntaxAnalysisError = new LoggingErrorListener();
    MacroSymbolTable macroSymbolTable = null;
    try {
      macroSymbolTable =
          antlrFacade.compileContentWithGrammarMacroST(content, grammarName, lexicalAnalysisError,
              syntaxAnalysisError);
    } catch (Exception e) {
      journal.add(Level.ERROR, "N'arrive pas à compiler : ", e.getMessage());
    }

    if (isErrorPreserved) {
      lexicalGrammarErrors.addAll(lexicalAnalysisError.getErrorsList());
      syntaxGrammarErrors.addAll(syntaxAnalysisError.getErrorsList());
    }

    return macroSymbolTable;
  }

  private String resolve(List<MacroSymbol> symbols) {

    for (int i = 0; i < symbols.size(); i++) {
      MacroSymbol symbol = symbols.get(i);
      if (symbol.getType() == MacroSymbolType.CONDITIONAL) {
        ConditionalSymbol cond = (ConditionalSymbol) symbol;
        cond.verification(variables);
        String resultCond = cond.render(variables);
        if (!resultCond.isEmpty()) {
          MacroSymbolTable st = compileString(resultCond, false);
          List<MacroSymbol> subsets = st.getTable();
          resolve(subsets);
        }
      } else if (symbol.getType() == MacroSymbolType.CONTENT) {
        ContentSymbol content = (ContentSymbol) symbol;
        result.add(content.render());
      } else if (symbol.getType() == MacroSymbolType.INCLUDE) {
        IncludeSymbol incl = (IncludeSymbol) symbol;
        incl.verification(includes);
        String inclResult = incl.render(includes);
        if (!inclResult.isEmpty()) {
          MacroSymbolTable st = compileString(inclResult, true);
          List<MacroSymbol> subsets = st.getTable();
          resolve(subsets);
        }
      } else if (symbol.getType() == MacroSymbolType.SCOPE) {
        result.add(resolveScope(symbols.subList(i, symbols.size())));
      } else if (symbol.getType() == MacroSymbolType.SUBSTITUTION) {
        SubstitutionSymbol sub = (SubstitutionSymbol) symbol;
        sub.verification(variables);
        result.add(sub.render(variables));
      } else if (symbol.getType() == MacroSymbolType.VARIABLE) {
        VariableSymbol var = (VariableSymbol) symbol;
        var.verification(variables);
        variables.put(var.getKey(), var.getValue());
        result.add(var.render());
      } else {
        symbolErrors.add(new MacroException(symbol, "Ce type de macro n'existe pas."));
      }
    }

    symbolErrors.removeAll(Collections.singleton(null));
    return String.join("", result);
  }

  private String resolveScope(List<MacroSymbol> symbols) {
    String result = "";
    MacroSymbol begin = symbols.get(0);
    List<MacroSymbol> subsets = new ArrayList<MacroSymbol>();
    for (int j = 1; j < symbols.size(); j++) {
      MacroSymbol subSymbol = symbols.get(j);
      subsets.add(subSymbol);
      if (begin.getIdObject() == subSymbol.getIdObject()) {
        result = resolveScope(subsets.subList(1, subsets.size() - 1));
      }
    }
    return result;
  }

  private void toIntermediateCode() {
    this.out = resolve(macroSymbolTable.getTable());
  }

  public String getIn() {
    return in;
  }

  public String getOut() {
    return out;
  }

  public void setIn(String in) {
    this.in = in;
  }

  public void setOut(String out) {
    this.out = out;
  }

  public String getErrors() {
    return lexicalGrammarErrors.toString() + syntaxGrammarErrors.toString()
        + symbolErrors.toString();
  }

  public Journal getJournal() {

    for (String error : lexicalGrammarErrors) {
      journal.add(Level.ERROR, error);
    }
    for (String error : syntaxGrammarErrors) {
      journal.add(Level.ERROR, error);
    }

    for (MacroException error : symbolErrors) {
      journal.add(Level.ERROR, error.getMessage());
    }

    return journal;
  }

}
