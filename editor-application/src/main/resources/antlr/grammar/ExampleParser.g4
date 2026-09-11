parser grammar ExampleParser;

@header {
import ca.mbjolin.antlr.editor.EditorParser;
}

options {
  tokenVocab = ExampleLexer;
  superClass = EditorParser;
  /*
  contextSuperClass = EditorParser;
  * ne semble pas servir pour mon cas.
  */
}

/*
Dans le cas qu'on veut utiliser des règles d'une autre grammaire.
import EditorParser;

element: OS elementval CS;
*/

bof : (STRING POUR)*;
