/** Taken from "The Definitive ANTLR 4 Reference" by Terence Parr */

// Derived from https://json.org

// $antlr-format alignTrailingComments true, columnLimit 150, minEmptyLines 1, maxEmptyLinesToKeep 1, reflowComments false, useTab false
// $antlr-format allowShortRulesOnASingleLine false, allowShortBlocksOnASingleLine true, alignSemicolons hanging, alignColons hanging

parser grammar JSONParser;

@header {
import ca.mbjolin.antlr.editor.EditorParser;
}

options {
  tokenVocab = JSONLexer;
  superClass = EditorParser;
}

json
    : value EOF
    ;

obj
    : AO pair (COMMA pair)* AF
    | AO AF
    ;

pair
    : STRING TWOPOINT value
    ;

arr
    : CO value (COMMA value)* CF
    | CO CF
    ;

value
    : STRING
    | NUMBER
    | obj
    | arr
    | TRUE
    | FALSE
    | NULL
    ;