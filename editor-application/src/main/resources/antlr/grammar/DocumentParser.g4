parser grammar DocumentParser;

@header {
import ca.mbjolin.editor.process._1_translation.grammar.Symbol;
import ca.mbjolin.editor.process._1_translation.grammar.SymbolTable;
import ca.mbjolin.editor.process._1_translation.grammar.SymbolType;
import ca.mbjolin.editor.process._1_translation.grammar.SymbolAttribut;
}

@parser::members {
public SymbolTable symbolTable = new SymbolTable();
}

options {
  tokenVocab = DocumentLexer;
}

document
:
  meta* section*
  {
Symbol doc = new Symbol(SymbolType.DOCUMENT, _localctx);
doc.addAttribut(SymbolAttribut.IDENTIFIANT, "");
symbolTable.add(_localctx, doc);
  }

;

meta
:
  OM identifiant valeur CM
  {
Symbol meta = new Symbol(SymbolType.META, _localctx);
meta.addAttribut(SymbolAttribut.IDENTIFIANT, _localctx.identifiant());
meta.addAttribut(SymbolAttribut.VALEUR, _localctx.valeur());
symbolTable.add(_localctx, meta);
}

;

identifiant
:
  KEY
;

valeur
:
  STRING
;

section
:
  OS
  (
    identifiant paragraphe*
    | section*
  ) CS
  {
Symbol section = new Symbol(SymbolType.SECTION, _localctx);
section.addAttribut(SymbolAttribut.IDENTIFIANT, _localctx.identifiant());
symbolTable.add(_localctx, section);
}

;

paragraphe
:
  OP identifiant
  (
    contenu
    | citation
    | predicat
  )* CP
  {
  Symbol paragraphe = new Symbol(SymbolType.PARAGRAPHE, _localctx);
  paragraphe.addAttribut(SymbolAttribut.IDENTIFIANT, _localctx.identifiant());
  symbolTable.add(_localctx, paragraphe);
  }

;

contenu
:
  OCO identifiant description CCO
  {
    Symbol contenu = new Symbol(SymbolType.CONTENU, _localctx);
    contenu.addAttribut(SymbolAttribut.IDENTIFIANT, _localctx.identifiant());
    contenu.addAttribut(SymbolAttribut.DESCRIPTION, _localctx.description());
    symbolTable.add(_localctx, contenu);
  }

;

citation
:
  OC
  identifiant
  (
     description
    | source
    | description source
  ) CC
  {
    Symbol citation = new Symbol(SymbolType.CITATION, _localctx);
    citation.addAttribut(SymbolAttribut.IDENTIFIANT, _localctx.identifiant());
    citation.addAttribut(SymbolAttribut.DESCRIPTION, _localctx.description());
    symbolTable.add(_localctx, citation);
  }

;

/* Réfléchir pour la description formelle plus tard. */
predicat
:
  OPR
  identifiant
  (
    description
    | source
    | description source
  ) CPR
  {
    Symbol predicat = new Symbol(SymbolType.REGLELOGIQUE, _localctx);
    predicat.addAttribut(SymbolAttribut.IDENTIFIANT, _localctx.identifiant());
    predicat.addAttribut(SymbolAttribut.DESCRIPTION, _localctx.description());
    symbolTable.add(_localctx, predicat);
  }

;

description
:
  STRING
;

source
:
  OSOURCE
  (
    identifiant
    | description
  ) CSOURCE
  {
    Symbol source = new Symbol(SymbolType.SOURCE, _localctx);
    source.addAttribut(SymbolAttribut.IDENTIFIANT, _localctx.identifiant());
    source.addAttribut(SymbolAttribut.DESCRIPTION, _localctx.description());
    symbolTable.add(_localctx, source);
  }

;

