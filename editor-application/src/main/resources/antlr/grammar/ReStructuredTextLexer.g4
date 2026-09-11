// $antlr-format alignTrailingComments true, columnLimit 150, minEmptyLines 1, maxEmptyLinesToKeep 1, reflowComments false, useTab false
// $antlr-format allowShortRulesOnASingleLine false, allowShortBlocksOnASingleLine true, alignSemicolons hanging, alignColons hanging

lexer grammar ReStructuredTextLexer;

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
SectionSeparator
    : (Minus | Equal | Plus | Hat) (Minus | Equal | Plus | Hat) (Minus | Equal | Plus | Hat)+
    ;

Literal
    : Colon LineBreak LineBreak* Colon Colon
    ;

TimeStar
    : Numbers Star
    | 'x' Star
    ;

Alphabet
    : [A-Za-z]+
    ;

Numbers
    : [0-9]+
    ;

Quote
    : Colon Colon
    ;

SquareLeft
    : '['
    ;

SquareRight
    : ']'
    ;

RoundLeft
    : '('
    ;

RoundRight
    : ')'
    ;

AngleLeft
    : '<'
    ;

AngleRight
    : '>'
    ;

Hat
    : '^'
    ;

QuotationDouble
    : '"'
    ;

QuotationSingle
    : '\''
    ;

Dot
    : '.'
    ;

SemiColon
    : ';'
    ;

Colon
    : ':'
    ;

Equal
    : '='
    ;

Plus
    : '+'
    ;

Minus
    : '-'
    ;

Block
    : '|'
    ;

Comment
    : ('.. ' LineBreak?)
    | ('..' LineBreak)
    ;

UnderScore
    : '_'
    ;

BackTick
    : '`'
    ;

Star
    : '*'
    ;

Space
    : ' '
    | '\t'
    ;

LineBreak
    : '\r'? '\n'
    ;

Any
    : .
    ;

Doc : ':doc:'
;