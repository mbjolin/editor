lexer grammar RuleLexer;

DONC:
  'donc'
;

ET:
  'et'
;

OU:
  'ou'
;

OP
:
  '('
;

SP
:
  ','
;

FP
:
  ')'
;

QUOTE
:
  '\''
;

NE
:
  'n\''
;

ESTVIDE
:
  'est' NAMESEP 'vide'
;

ESTREMPLI
:
  'est' NAMESEP 'rempli'
;

ESTUNIQUE
:
  'est' NAMESEP 'unique'
;

ESTORDONNE
:
  'est' NAMESEP 'ordonné'
;

ESTEGALE
:
  'est' NAMESEP 'égale'
;

EXISTE
:
  'existe'
;

DEVIENT
:
  'devient'
;

SYSTEME
:
  'système'
;

PROJET
:
  'projet'
;

DOC
:
  'doc'
;

NIVEAU
:
  'niveau'
;

DOMAINE
:
  'domaine'
;

GENEREALEATOIRE
:
  'génère' NAMESEP 'aléatoire'
;

GENEREDATE
:
  'génère' NAMESEP 'date'
;

CONCATENATION
:
  'concatenation'
;

AFFILIATION
:
  'affiliation'
;

DOCUMENT:
  'document' |
  'document.identifiant' |
  'document.domaineParDefaut' |
  'document.niveauParDefaut' |
  'document.metas' |
  'document.sections' 
;

META:
  'meta' |
  'meta.identifiant' |
  'meta.valeur'
;

SECTION:
  'section' |
  'section.identifiant' |
  'section.paragraphes' |
  'section.sections'
;

PARAGRAPHE:
  'paragraphe' |
  'paragraphe.identifiant' |
  'paragraphe.domaine' |
  'paragraphe.niveau' |
  'paragraphe.contenus' |
  'paragraphe.citations' |
  'paragraphe.predicats' 
;

CONTENU:
  'contenu' |
  'contenu.identifiant' |
  'contenu.description'
;

CITATION:
  'citation' |
  'citation.identifiant' |
  'citation.description' | 
  'citation.sources'
;


PREDICAT:
  'predicat' |
  'predicat.identifiant' |
  'predicat.descriptionFormelle' |
  'predicat.descriptionNaturelle' | 
  'predicat.sources'
;

SOURCE:
  'source' |
  'source.identifiant' |
  'source.description' 
;


WORD
:
  [A-Za-z0-9-]+
;

COMMENT
:
  '//' ~[\r\n]* -> skip
;

WS
:
  (
    ' '
    | '\t'
    | '\r'
    | '\n'
  )+ -> skip
;

NAMESEP
:
  '-'
;
