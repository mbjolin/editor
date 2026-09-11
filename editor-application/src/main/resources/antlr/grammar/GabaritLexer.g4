lexer grammar GabaritLexer;

UNIQUE : 'est unique';

SECTION : 'section';

IDENTIFIANT : 'identifiant';

ORDONNER : 'est ordonnancé';

COMPOSER : 'est composé';

NOMMER : 'est nommé';

SEPARATEUR : (',' | 'ou' | 'et');

DV : [(];

FV : [)];

FR : [;];

INT : [0-9]+;

UUID : [0-9]+;

MOT : [A-Za-z0-9-]+;

COMMENT : '--' ~ [\r\n]* -> skip;

WS : (' ' |':' | '\t' | '\r' | '\n' )+ -> skip;


