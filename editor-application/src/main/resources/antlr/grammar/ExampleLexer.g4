lexer grammar ExampleLexer;

@header {
import ca.mbjolin.antlr.editor.EditorLexer;
}

options {
  superClass = EditorLexer;
}

/*
 * Lexer Rules
 */

FM : [;];

FP : [|];

POUR : [=];

/* à changer pour le vrai uuid. */
UUID : [0-9]+;

INT : [0-9]+;

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

COMMENT : '+' ~ [\r\n]* -> skip;

WS : [ \t\r\n] -> skip;