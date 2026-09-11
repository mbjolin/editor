package ca.mbjolin.editor.preprocess._2_macro.exception;

import ca.mbjolin.editor.preprocess._2_macro.grammar.MacroSymbol;

public class MacroException extends Exception {

  public MacroException(MacroSymbol macro, String message) {
    super(message);
  }
}
