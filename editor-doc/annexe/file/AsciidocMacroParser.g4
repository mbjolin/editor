parser grammar AsciidocMacroParser;
@header {
import java.util.Arrays;
import ca.mbjolin.editor.preprocess._2_macro.grammar.MacroSymbol;
import ca.mbjolin.editor.preprocess._2_macro.grammar.MacroSymbolTable;
import ca.mbjolin.editor.preprocess._2_macro.grammar.MacroSymbolType;
import ca.mbjolin.editor.preprocess._2_macro.grammar.ConditionalSymbol;
import ca.mbjolin.editor.preprocess._2_macro.grammar.ContentSymbol;
import ca.mbjolin.editor.preprocess._2_macro.grammar.IncludeSymbol;
import ca.mbjolin.editor.preprocess._2_macro.grammar.SubstitutionSymbol;
import ca.mbjolin.editor.preprocess._2_macro.grammar.ScopeSymbol;
import ca.mbjolin.editor.preprocess._2_macro.grammar.VariableSymbol;
}
@parser::members {
private List<MacroSymbolType> resolutionOrder = Arrays.asList(
  MacroSymbolType.VARIABLE,
  MacroSymbolType.INCLUDE,
  MacroSymbolType.SUBSTITUTION,
  MacroSymbolType.CONDITIONAL
);
public MacroSymbolTable macroSymbolTable = new MacroSymbolTable(resolutionOrder);
}
options {
    tokenVocab = AsciidocMacroLexer;
}
//https://stackoverflow.com/questions/25914154/antlr4-parse-a-file-line-by-line
//https://stackoverflow.com/questions/27951798/macro-expansion-in-antlr4
doc: content? ( macro content)* EOF;
content:
    (WORD | SYMBOL | WS | EOL | OPENBRACKET | CLOSEBRACKET)+ {
ContentSymbol content = new ContentSymbol(_localctx);
macroSymbolTable.add(content);
  }
;
/* Je dois considérer l'intérieur d'une condition comme un contenu,
 * donc il faut ignorer les symboles de macros. Il doit y avoir une solution
 * plus élégante.
 */
contentsymbolmacro:
    (
        WORD
        | SYMBOL
        | WS
        | EOL
        | OS
        | CS
        | OPENBRACKET
        | CLOSEBRACKET
        | DOT
        | OK
        | CK
    )+
;
contentmacro: ( WORD | SYMBOL | WS | EOL | macro)+;
macro:
    {
    ScopeSymbol scopebegin = new ScopeSymbol(_localctx, true);
    macroSymbolTable.add(scopebegin);
  } (substitution | include | variable | conditional) {
    ScopeSymbol scopeend = new ScopeSymbol(_localctx, false);
    macroSymbolTable.add(scopeend);
  }
;
substitution:
    OS wordmacro CS {
SubstitutionSymbol substitution = new SubstitutionSymbol(_localctx, _localctx.wordmacro().getText());
macroSymbolTable.add(substitution);
  }
;
wordmacro: ( macro | WORD)+;
include:
    OI (macro | URI) OPENBRACKET CLOSEBRACKET {
IncludeSymbol include = new IncludeSymbol(_localctx, _localctx.URI().getText());
macroSymbolTable.add(include);
  }
;
//TRIM la valeur pour avoir un valeur de longeur 0.
variable:
    (
        OK key CNEG WS* {
  VariableSymbol variable = new VariableSymbol(_localctx, _localctx.key().getText(), null);
  macroSymbolTable.add(variable);
  }
        | ONEG key OK WS* {
  VariableSymbol variable = new VariableSymbol(_localctx, _localctx.key().getText(), null);
  macroSymbolTable.add(variable);
  }
        | OK key CK value {
  VariableSymbol variable = new VariableSymbol(_localctx, _localctx.key().getText(), _localctx.value().getText());
  macroSymbolTable.add(variable);
  }
    )
;
key: WORD;
value: ( WORD | SYMBOL | WS | macro)+;
conditional:
    OC wordmacro OPENBRACKET CLOSEBRACKET contentsymbolmacro CC wordmacro? OPENBRACKET CLOSEBRACKET
        {
ConditionalSymbol condition = new ConditionalSymbol(_localctx, _localctx.wordmacro().get(0).getText(),
 _localctx.contentsymbolmacro().getText());
macroSymbolTable.add(condition);
  }
;