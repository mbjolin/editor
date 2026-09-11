package ca.mbjolin.editor.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.misc.ParseCancellationException;

/* https://stackoverflow.com/questions/18132078/handling-errors-in-antlr4 */
public class LoggingErrorListener extends BaseErrorListener {

  private Map<String, String> errors = new HashMap<>();

  @Override
  public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
      int charPositionInLine, String msg, RecognitionException e)
      throws ParseCancellationException {
    String position = "ligne " + line + ":" + charPositionInLine;
    errors.put(position, msg);
  }

  public Map<String, String> getErrorsMap() {
    return errors;
  }

  public List<String> getErrorsList() {
    List<String> errorList = errors.keySet().stream()
        .map(key -> key + " - " + errors.get(key))
        .collect(Collectors.toList());
    return errorList;
  }

  public String getErrors() {
    String mapAsString = errors.keySet().stream()
        .map(key -> key + " - " + errors.get(key))
        .collect(Collectors.joining("\n"));
    return mapAsString;
  }
}
