package ca.mbjolin.editor.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum Etape {
  EPURER("EPURER", "épuration"),
  FORMALISER("FORMALISER", "formalisation"),
  PRESENTER("PRESENTER", "présentation");

  private static Map<String, Etape> map = new ConcurrentHashMap<>();
  private String text;
  private String uiText;

  Etape(String text, String uiText) {
    this.text = text;
    this.uiText = uiText;
  }
  
  public static String getEtapeValue() {
    return "épuration, formalisation, présentation";
  }

  public static Etape fromString(String uiText) {
    if (map.isEmpty()) {
      for (Etape type : Etape.values()) {
        map.put(type.uiText, type);
      }
    }
    return map.get(uiText);
  }

  public String getText() {
    return text;
  }
  
  public String getUiText() {
    return uiText;
  }
}
