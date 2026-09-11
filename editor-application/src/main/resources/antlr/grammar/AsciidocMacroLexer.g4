
// $antlr-format alignTrailingComments true, allowShortRulesOnASingleLine true, maxEmptyLinesToKeep 0, columnLimit 100

lexer grammar AsciidocMacroLexer;
OC: 'ifdef::';
CC: 'endif::';
OPENBRACKET: '[';
CLOSEBRACKET: ']';
OS: '{';
CS: '}';
OI: 'include::';
// C'est comme une string avec un \ en plus.
URI: ( SLASH? ( ALPHANUMERIC | SYMBOL))+ ( DOT ALPHANUMERIC+);
CK: COLON SPACE;
OK: COLON;
ONEG: COLON EXCLAMATION;
CNEG: EXCLAMATION COLON;
COMMENT: '//' ~'/' ~[\r\n]*? EOL+ -> skip;
//https://stackoverflow.com/questions/51576395/antlr4-grammar-extraneous-input-with-single-space
WS: ( SPACE | TABULATION)+;
EOL: '\r'? '\n';
WORD: ( ALPHANUMERIC | SYMBOL)+;
SYMBOL:
    (
        '-'
        | '_'
        | '.'
        | '='
        | 'à'
        | 'é'
        | 'ê'
        | 'è'
        | '\''
        | 'â'
        | ','
        | '`'
        | ':='
        | ';'
        | ')'
        | '('
        | '+'
        | '"'
        | '%'
    )
;
fragment ALPHANUMERIC: [a-zA-Z~0-9];
fragment SPACE: ' ';
fragment TABULATION: '\t';
fragment SLASH: '/';
fragment DOT: '.';
fragment COLON: ':';
fragment EXCLAMATION: '!';
fragment LOWERCASE: [a-z];