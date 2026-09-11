parser grammar RuleParser;

options {
  tokenVocab = RuleLexer;
}

@header {
import ca.mbjolin.editor.process._2_intermediaterepresentation.regle.Regle;
}

@parser::members {
public Regle regle = new Regle();
}

regle
:
  implication* EOF
;
// Implication est naturellement une fonction, mais je ne veux 
// pas de récursion dans les règles. Est-ce que c'est mal? c<est ok

//À cause que je ne suis pas LL1 je peux évaluer toute l'expression
//au lieu d'arrêter à la première OR false. C<est ok


//verification (and and or) => modification (and only)
//modification (and) => verification (and and or)
implication
:
  groupefonction
  (
    adverbe groupefonction
  )?
  ImplicationRuleSymbol implication = new ImplicationRuleSymbol(_localctx, _localctx.groupefonction().get(0).getText(), _localctx.groupefonction().get(1).getText())
;

adverbe
:
  DONC
;

groupefonction
:
  fonction
  (
    conjonction fonction
  )*
  | fonction
  (
    disjonction fonction
  )*
;

conjonction
:
  ET
;

disjonction
:
  OU
;

fonction
:
  portee parametres negation? nomfonction
;

parametres
:
  OP parametre
  (
    SP parametre
  )* FP
;

parametre
:
  elementmodele
  | generate
  | QUOTE WORD* QUOTE
;

negation
:
  NE
;

nomfonction
:
   ESTVIDE
  | ESTREMPLI
  | ESTUNIQUE
  | ESTORDONNE
  | ESTEGALE
  | EXISTE
  | DEVIENT
;

portee
:
  SYSTEME
  | PROJET
  | DOC
  | NIVEAU
  | DOMAINE
;

generate
:
  parametres
  (
    GENEREALEATOIRE
    | GENEREDATE
    | CONCATENATION
    | AFFILIATION
  )
;

elementmodele
:
  | DOCUMENT
  | META
  | SECTION
  | PARAGRAPHE
  | CONTENU
  | CITATION
  | PREDICAT
  | SOURCE
;


