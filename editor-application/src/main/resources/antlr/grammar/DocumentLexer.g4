lexer grammar DocumentLexer;

/*
 * Lexer Rules
 * https://tomassetti.me/antlr-mega-tutorial/
 * O pour ouverture, C pour clôture.
 */
OS
:
  'beginsection'
;

CS
:
  'endsection'
;

OM
:
  'beginmeta'
;

SEPM
:
  '='
;

CM
:
  'endmeta'
;

OP
:
  'beginparagraphe'
;

CP
:
  'endparagraphe'
;

OV
:
  ["]
;

CV
:
  ["]
;

OCO
:
  'begincontent'
;

CCO
:
  'endcontent'
;

OC
:
  'begincite'
;

CC
:
  'endcite'
;

OPR
:
  'beginpredicat'
;

CPR
:
  'endpredicat'
;

OSOURCE
:
  '['
;

CSOURCE
:
  ']'
;

WORD
:
  (
    LOWERCASE
    | UPPERCASE
    | SYMBOL
  )+
;

KEY
:
  (
    WORD
    | DIGIT
  )+
;

fragment
DIGIT
:
  [0-9]+
;

//., sur les énumérations et sousscalaire pourrait poser problème.
// pas utilisé, je pourrai retirer.
NUMBER
:
  DIGIT+
  (
    [.,] DIGIT+
  )?
;

fragment
LOWERCASE
:
  [a-z]
;

fragment
UPPERCASE
:
  [A-Z]
;
//Si le - est utiliser dans les operations cela peut causer des problèmes.
SYMBOL
:
  (
    '-'
    | '_'
  )
;

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

WHITESPACE
:
  (
    '\u0009' // character tabulation \t

    | '\u000A' // line feed \n

    | '\u000B' // line tabulation

    | '\u000C' // form feed

    | '\u000D' // carriage return \r

    | '\u0020' // space

    | '\u0085' // next line

    | '\u00A0' // no-break space

    | '\u1680' // ogham space mark

    | '\u180E' // mongolian vowel separator

    | '\u2000' // en quad

    | '\u2001' // em quad

    | '\u2002' // en space

    | '\u2003' // em space

    | '\u2004' // three-per-em space

    | '\u2005' // four-per-em space

    | '\u2006' // six-per-em space

    | '\u2007' // figure space

    | '\u2008' // punctuation space

    | '\u2009' // thin space

    | '\u200A' // hair space

    | '\u200B' // zero width space

    | '\u200C' // zero width non-joiner

    | '\u200D' // zero width joiner

    | '\u2028' // line separator

    | '\u2029' // paragraph separator

    | '\u202F' // narrow no-break space

    | '\u205F' // medium mathematical space

    | '\u2060' // word joiner

    | '\u3000' // ideographic space

    | '\uFEFF' // zero width non-breaking space

  ) -> skip
;
