/*
 * Inspiré de https://github.com/venkatperi/asciidoc-antlr/blob/master/src/main/antlr/AsciidocLexer.g4
 */

// $antlr-format alignTrailingComments true, allowShortRulesOnASingleLine true, maxEmptyLinesToKeep 0, columnLimit 100

parser grammar AsciidocParser;
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
    tokenVocab = AsciidocLexer;
}
///////////////////////
// start rule
asciidoc:
    header section* EOF {
Symbol doc = new Symbol(SymbolType.DOCUMENT, _localctx);
doc.addAttribut(SymbolAttribut.IDENTIFIANT, _localctx.header().doc_title_line());
symbolTable.add(_localctx, doc);
  }
;
///////////////////////
// doc header
header:
    (header_line | EOL)* doc_title_line author_rev? header_line* END_OF_HEADER
;
// You cannot have a revision line without an author line.
author_rev: authors revision?;
header_line: global_attr | pp_directive;
///////////////////////
// doc title
doc_title_line:
    H0 //doc_title_def
;
///////////////////////
// doc author / rev
authors: AUTHOR;
revision: REVISION;
///////////////////////
// global_attrs
global_attr: ATTR_START attr_def ATTR_EOL;
attr_def: attr_unset ATTR_SEP | attr_set;
attr_set:
    attr_id ATTR_SEP attr_value? attr_uid? {
Symbol meta = new Symbol(SymbolType.META, _localctx);
meta.addAttribut(SymbolAttribut.IDENTIFIANT, _localctx.attr_uid());
meta.addAttribut(SymbolAttribut.ETIQUETTE, _localctx.attr_id());
meta.addAttribut(SymbolAttribut.VALEUR, _localctx.attr_value());
symbolTable.add(_localctx, meta);
}
;
attr_unset: ATTR_UNSET attr_id | attr_id ATTR_UNSET;
attr_id: ATTR_ID;
attr_value: ATTR_VALUE;
attr_uid: ATTR_UID;
///////////////////////
// doc preamble
preamble: body_item* SECTION_END;
///////////////////////
// doc sections
section:
    (
        section_header section_body {
    Symbol paragraphe = new Symbol(SymbolType.PARAGRAPHE, _localctx.section_body());
    symbolTable.add(_localctx.section_body(), paragraphe);
    } section* (CUSTOM_END_SECTION | SECTION_END | EOF)
    ) {
  Symbol section = new Symbol(SymbolType.SECTION, _localctx);
  section.addAttribut(SymbolAttribut.IDENTIFIANT, _localctx.section_header().section_title().section_id());
  section.addAttribut(SymbolAttribut.ETIQUETTE, _localctx.section_header().section_title());
  symbolTable.add(_localctx, section);
  }
;
section_header: block_attr_line* section_title;
section_title:
    SECTION_TITLE_START SECTION_TITLE_CONTENT section_id? SECTION_TITLE_EOL+
;
section_id: SECTION_ID;
///////////////////////
// element attributes
block_attr_line:
    BLOCK_ATTR_START block_attr (BLOCK_ATTR_COMMA block_attr)* BLOCK_ATTR_END BLOCK_ATTR_EOL
    | anchor
;
block_attr: block_named_attr | block_positional_attr;
block_positional_attr:
    block_pos_prefixed_attr*
    | block_attr_id //style etc
;
block_pos_prefixed_attr:
    block_pos_attr_role
    | block_pos_attr_id
    | block_pos_attr_id
;
block_pos_attr_role: BLOCK_ATTR_TYPE_ROLE block_attr_id;
block_pos_attr_id: BLOCK_ATTR_TYPE_ID block_attr_id;
block_pos_attr_option: BLOCK_ATTR_TYPE_OPTION block_attr_id;
block_named_attr:
    block_attr_id BLOCK_ATTR_ASSIGN block_attr_value
;
block_attr_id: BLOCK_ATTR_ID;
block_attr_value: BLOCK_ATTR_VALUE;
///////////////////////
// section content
section_body: body_item*;
body_item:
    block_attr_line* block_title_line? (paragraph | delim_block)
;
paragraph:
    BLOCK_PARA {
  Symbol contenu = new Symbol(SymbolType.CONTENU, _localctx);
  //contenu.addAttribut(SymbolAttribut.IDENTIFIANT, "paragraph_id");
  contenu.addAttribut(SymbolAttribut.DESCRIPTION, _localctx);
  symbolTable.add(_localctx, contenu);
  }
;
///////////////////////
// block/element title mode
block_title_line: BLOCK_TITLE_START block_title;
block_title: BLOCK_TITLE_TEXT BLOCK_TITLE_EOL;
///////////////////////
// delimited blocks
delim_block:
    (
        sidebar_block
        | comment_block
        | fenced_block
        | example_block
        | listing_block
        | literal_block
        | pass_block
        | verse_block
        | table_block
        | anon_block
    )
;
table_block:
    BLOCK_TABLE_START delim_block_content DELIM_BLOCK_END
;
comment_block:
    BLOCK_COMMENT_START delim_block_content DELIM_BLOCK_END
;
fenced_block:
    BLOCK_FENCED_START delim_block_content DELIM_BLOCK_END {
  Symbol reglelogique = new Symbol(SymbolType.REGLELOGIQUE, _localctx);
  reglelogique.addAttribut(SymbolAttribut.DESCRIPTION, _localctx.delim_block_content());
  symbolTable.add(_localctx, reglelogique);
  }
;
example_block:
    BLOCK_EXAMPLE_START delim_block_content DELIM_BLOCK_END
;
listing_block:
    BLOCK_LISTING_START delim_block_content DELIM_BLOCK_END
;
literal_block:
    BLOCK_LITERAL_START delim_block_content DELIM_BLOCK_END
;
pass_block:
    BLOCK_PASS_START delim_block_content DELIM_BLOCK_END {
  Symbol reglelogique = new Symbol(SymbolType.REGLELOGIQUE, _localctx);
  reglelogique.addAttribut(SymbolAttribut.DESCRIPTIONF, _localctx.delim_block_content());
  symbolTable.add(_localctx, reglelogique);
  }
;
verse_block:
    BLOCK_VERSE_START delim_block_content DELIM_BLOCK_END {
  Symbol citation = new Symbol(SymbolType.CITATION, _localctx);
  citation.addAttribut(SymbolAttribut.DESCRIPTION, _localctx.delim_block_content());
  symbolTable.add(_localctx, citation);
  }
;
sidebar_block:
    BLOCK_SIDEBAR_START delim_block_content DELIM_BLOCK_END
;
anon_block:
    BLOCK_ANON_START delim_block_content DELIM_BLOCK_END
;
delim_block_content: DELIM_BLOCK_LINE*;
///////////////////////
// conditional pre-processor directives
pp_directive:
    PPD_START ppd_attr (PPD_ATTR_SEP ppd_attr)* ppd_content
;
ppd_attr: PPD_ATTR_ID;
ppd_content:
    PPD_CONTENT_SINGLELINE
    | PPD_CONTENT_START PPD_CONTENT
;
///////////////////////
// anchor
anchor: BLOCK_ANCHOR;