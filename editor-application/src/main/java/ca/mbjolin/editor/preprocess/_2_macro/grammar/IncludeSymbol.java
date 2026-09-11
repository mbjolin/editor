package ca.mbjolin.editor.preprocess._2_macro.grammar;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.antlr.v4.runtime.ParserRuleContext;
import org.apache.commons.io.FileUtils;

import ca.mbjolin.editor.preprocess._2_macro.exception.MacroException;
import io.quarkus.logging.Log;

public class IncludeSymbol extends MacroSymbol {

  String content;

  String uriString;

  File file;

  public <T extends ParserRuleContext> IncludeSymbol(T ctx, String uri) {
    type = MacroSymbolType.INCLUDE;
    this.setIdObject(ctx.hashCode());
    fillParent(ctx);
    content = ctx.getText();
    uriString = uri;
  }

  public MacroException verification(Map<String, String> includes) {
    MacroException exception = null;

    if (!includes.containsKey(uriString)) {
      try {
        URI uri = new URI("file:" + uriString);
        file = new File(uri);
        if (!file.exists()) {
          exception = new MacroException(this, "Le fichier n'existe pas.");
        }
      } catch (URISyntaxException e) {
        exception = new MacroException(this, e.getMessage());
      }
    }
    return exception;
  }

  public String render(Map<String, String> includes) {
    String result = "";
    if (includes.containsKey(uriString)) {
      result = includes.get(uriString);
    } else {
      try {
        result = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
      } catch (IOException e) {
        Log.error("Problème de lecture de fichier.");
      }
    }
    return result;
  }

}
