/** Taken from "The Definitive ANTLR 4 Reference" by Terence Parr */

// Derived from https://json.org

// $antlr-format alignTrailingComments true, columnLimit 150, minEmptyLines 1, maxEmptyLinesToKeep 1, reflowComments false, useTab false
// $antlr-format allowShortRulesOnASingleLine false, allowShortBlocksOnASingleLine true, alignSemicolons hanging, alignColons hanging
//https://stackoverflow.com/questions/75651784/specify-superclass-options-when-its-combined-grammar
lexer grammar JSONLexer;

@header {
import ca.mbjolin.antlr.editor.EditorLexer;
}

options {
  superClass = EditorLexer;
}

COMMA : ',';

TWOPOINT : ':';

TRUE : 'true';

FALSE : 'false';

NULL : 'null';

AO : '{';

AF : '}';

CO : '[';

CF : ']';

STRING
    : '"' (ESC | SAFECODEPOINT)* '"'
    ;

fragment ESC
    : '\\' (["\\/bfnrt] | UNICODE)
    ;

fragment UNICODE
    : 'u' HEX HEX HEX HEX
    ;

fragment HEX
    : [0-9a-fA-F]
    ;

fragment SAFECODEPOINT
    : ~ ["\\\u0000-\u001F]
    ;

NUMBER
    : '-'? INT ('.' [0-9]+)? EXP?
    ;

fragment INT
    // integer part forbids leading 0s (e.g. `01`)
    : '0'
    | [1-9] [0-9]*
    ;

// no leading zeros

fragment EXP
    // exponent number permits leading 0s (e.g. `1e01`)
    : [Ee] [+-]? [0-9]+
    ;

COMMENT : '//' ~ [\r\n]* -> skip;

WS
    : [ \t\n\r]+ -> skip
    ;
