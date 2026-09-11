// $antlr-format alignTrailingComments true, columnLimit 150, minEmptyLines 1, maxEmptyLinesToKeep 1, reflowComments false, useTab false
// $antlr-format allowShortRulesOnASingleLine false, allowShortBlocksOnASingleLine true, alignSemicolons hanging, alignColons hanging
parser grammar ReStructuredTextParser;

// Copyright (C) 2011 Bart Kiers
// Copyright (C) 2017 Lex Li
//
// Permission is hereby granted, free of charge, to any person obtaining a copy of this
// software and associated documentation files (the "Software"), to deal in the Software
// without restriction, including without limitation the rights to use, copy, modify, merge,
// publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
// to whom the Software is furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all copies or
// substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
// INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
// PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
// FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
// DEALINGS IN THE SOFTWARE.

/*
 *  A grammar for reStructuredText language written in ANTLR v4.
 *  This is just a snapshot of its 0.9 release.
 *  You can find the latest release from the official repo, https://github.com/lextm/restructuredtext-antlr
 *  Issues and pull requests should also go to the official repo.
 */
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
  tokenVocab = ReStructuredTextLexer;
}

parse
:
  (
    element
    | empty_line
  )+? EOF
  {
Symbol doc = new Symbol(SymbolType.DOCUMENT, _localctx);
doc.addAttribut(SymbolAttribut.IDENTIFIANT, "");
symbolTable.add(_localctx, doc);
  }

;

element
:
  section
  | sectionElement
;

sectionElement
:
  (
    listItemBullet
    | listItemEnumerated
    | paragraph
    | lineBlock
    | comment
  )
  {
  Symbol paragraphe = new Symbol(SymbolType.PARAGRAPHE, _localctx);
  paragraphe.addAttribut(SymbolAttribut.IDENTIFIANT, "");
  symbolTable.add(_localctx, paragraphe);
  }

;

comment
:
  Space* Comment Space*
  (
    commentLineNoBreak commentParagraphs?
  )?
;

commentParagraphs
:
  main = commentParagraph commentRest
;

commentRest
:
  (
    empty_line commentParagraph
  )*
;

commentParagraph
:
  commentLine+
;

commentLineNoBreak
:
  commentLineAtoms
;

commentLine
:
  LineBreak Space Space Space commentLineNoBreak
;

commentLineAtoms
:
  ~( LineBreak )+
;

paragraph
:
  lines
  {
    Symbol contenu = new Symbol(SymbolType.CONTENU, _localctx);
    contenu.addAttribut(SymbolAttribut.DESCRIPTION, _localctx.lines());
    symbolTable.add(_localctx, contenu);
  }

;

section
:
  (
    LineBreak overline = SectionSeparator
  )? title LineBreak? SectionSeparator
  (
    LineBreak
  )* sectionElement*
  {
  Symbol section = new Symbol(SymbolType.SECTION, _localctx);
  section.addAttribut(SymbolAttribut.IDENTIFIANT, _localctx.title());
  symbolTable.add(_localctx, section);
  }

;

title
:
  LineBreak textStart
  | LineBreak lineSpecial Space+
  (
    paragraphNoBreak
  )?
  | lineNormal
  | lineStar
;

lineBlock
:
  LineBreak lineBlockLine LineBreak? lineBlockLine*
;

lineBlockLine
:
  Block Space indentation? span*? starText
  | Block Space indentation? span+
;

listItemBullet
:
  bulletCrossLine
  | bulletSimple
  | LineBreak Space* special =
  (
    Minus
    | Plus
  )
;

bulletCrossLine
:
  LineBreak Space* bullet Space*
  (
    paragraph+
  )?
;

bulletSimple
:
  LineBreak Space* bullet Space+ paragraphNoBreak paragraph*
;

bullet
:
  Star
  | Minus
  | Plus
;

listItemEnumerated
:
  LineBreak enumerated = lineSpecial Space+
  (
    paragraphNoBreak paragraph*
  )?
;

paragraphNoBreak
:
  lineNoBreak lines*
;

lineNoBreak
:
  indentation? spanLineStartNoStar span*?
;

lines
:
  linesStar
  | linesNormal
;

linesNormal
:
  lineNormal
  (
    linesStar
    | linesNormal?
  )
;

linesStar
:
  lineStar
  | lineStar lineNoBreak linesNormal??
  | lineStar lineNoBreak linesStar
;

lineNormal
:
  LineBreak indentation? spanLineStartNoStar+?
  (
    span*? spanNoStar+?
  )?
  | lineSpecial
;

lineStar
:
  LineBreak indentation? spanLineStartNoStar*? starText
  | LineBreak indentation? text_fragment+ starText
;

lineSpecial
:
  Numbers Dot
  | LineBreak indentation? Numbers
  | LineBreak indentation? SectionSeparator
  (
    Space+ SectionSeparator
  ) Space* // for table.
  //|  Alphabet Dot

;

empty_line
:
  LineBreak Space*
;

indentation
:
  Space+
;

spanLineStartNoStar
:
  reference
  | referenceIn
  | hyperlinkTarget
  | hyperlink
  | hyperlinkDoc
  | backTickText
  | quotedLiteral
  | textLineStart
;

textLineStart
:
  lineStart_fragment+ text_fragment*
;

lineStart_fragment
:
  (
    Minus ~( Space | LineBreak | Star )
  )
  |
  (
    Plus ~( Space | Star )
  )
  |
  (
    Numbers Dot ~( Space | LineBreak | Star )
  )
  |
  (
    Numbers ~( Dot | LineBreak | Star )
  )
  //|  (Alphabet Dot ~(Space | LineBreak | Star))

  |
  (
    Alphabet Dot
  )
  |
  (
    Block ~( Space | Star )
  )
  |
  (
    UnderScore ~( Space | Star )
  )
  |
  (
    Alphabet ~( Dot | LineBreak | Star )
  )
  | Alphabet
  | separator separator
  | TimeStar
  | SquareLeft
  | SquareRight
  | RoundLeft
  | RoundRight
  | SemiColon
  | Colon
  | QuotationDouble
  | QuotationSingle
  | Dot
  | UnderScore
  | AngleLeft
  | AngleRight
  | Any
;

text
:
  textStart+ text_fragment*
;

textStart
:
  forcedText
  | lineStart_fragment
  | text_fragment_start text_fragment_start+
  | Space
;

forcedText
:
  RoundLeft Star RoundRight
  | SquareLeft Star SquareRight
  | QuotationSingle Star QuotationSingle
  | QuotationSingle QuotationDouble Star QuotationDouble QuotationSingle
;

spanNoStar
:
  reference
  | referenceIn
  | hyperlinkTarget
  | hyperlink
  | hyperlinkDoc
  | backTickText
  | quotedLiteral
  | text
;

span
:
  starText
  | spanNoStar
;

quotedLiteral
:
  AngleRight Space lineNoBreak
  {
    Symbol citation = new Symbol(SymbolType.CITATION, _localctx);
    citation.addAttribut(SymbolAttribut.DESCRIPTION, _localctx.lineNoBreak());
    symbolTable.add(_localctx, citation);
  }
;

text_fragment_start
:
  SemiColon
  | Numbers
  | Alphabet
  | Space
  | SquareLeft
  | SquareRight
  | RoundLeft
  | RoundRight
  | Colon
  | separator
  | AngleLeft
  | AngleRight
  | QuotationDouble
  | Dot
  | Star Space
  | Any
;

text_fragment
:
  text_fragment_start
  | forcedText
  | Block
  | Literal
  | Comment
  | Dot
  | Quote
;

starText
:
  Star+ LineBreak
  | Star+ starNoSpace starAtoms
  (
    LineBreak Star* starNoSpace starAtoms
  )* Star* LineBreak
  | Star+ starNoSpace starAtoms Star* LineBreak
  | Star+ Space+ starAtoms Star+ LineBreak
;

starAtoms
:
  starAtom*
  (
    Star* starAtom
  )*
;

starNoSpace
:
  ~( Star | LineBreak | Space | SectionSeparator )
;

starAtom
:
  ~( Star | LineBreak )
;

backTickText
:
  (
    Colon titled = Alphabet Colon
  )? body UnderScore?
;

body
:
  BackTick BackTick* backTickAtoms BackTick+
  | BackTick backTickNoSpace backTickAtoms BackTick+
  | BackTick BackTick
;

backTickAtoms
:
  backTickAtom+
;

backTickNoSpace
:
  ~( BackTick | LineBreak | Space )
;

backTickAtom
:
  ~( BackTick | LineBreak )
  | BackTick ~( BackTick | LineBreak )
;

reference
:
  Any+ UnderScore
;

referenceIn
:
  UnderScore hyperlinkAtom+ Colon Space url
;

hyperlinkTarget
:
  UnderScore Any+
;

hyperlink
:
  BackTick hyperlinkAtom+ Space AngleLeft url AngleRight BackTick UnderScore
  Space
;

hyperlinkDoc
:
  Doc BackTick hyperlinkAtom+ Space AngleLeft url AngleRight BackTick
  | Doc BackTick url BackTick
;

url
:
  urlAtom+
;

urlAtom
:
  ~( LineBreak | BackTick )
;

hyperlinkAtom
:
  ~( LineBreak | AngleLeft | AngleRight | BackTick | Star )
;

separator
:
  (
    Minus
    | Equal
    | Plus
    | Hat
  )
;
