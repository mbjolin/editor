package ca.mbjolin.editor.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum Etat {
  NOT_STARTED("NOT_STARTED", 0),
  EPURER_SUCCESS("EPURER_SUCCESS", 1),
  EPURER_FAIL("EPURER_FAIL", 1),
  FORMALISER_SUCCESS("FORMALISER_SUCCESS", 2),
  FORMALISER_FAIL("FORMALISER_FAIL", 2),
  FORMALISER_RULE_SUCCESS("FORMALISER_RULE_SUCCESS", 3),
  FORMALISER_RULE_FAIL("FORMALISER_RULE_FAIL", 3),
  PRESENTER_SUCCESS("PRESENTER_SUCCESS", 4),
  PRESENTER_FAIL("PRESENTER_FAIL", 4),
  ATELIER_SUCCESS("ATELIER_SUCCESS", 5),
  ATELIER_FAIL("ATELIER_FAIL", 5),
  FINISH("FINISH", 6);

  private static Map<String, Etat> map = new ConcurrentHashMap<>();
  private String text;
  private Integer order;

  Etat(String text, Integer order) {
    this.text = text;
    this.order = order;
  }

  public static Etat fromString(String text) {
    if (map.isEmpty()) {
      for (Etat type : Etat.values()) {
        map.put(type.text, type);
      }
    }
    return map.get(text);
  }

  public String getText() {
    return text;
  }
  
  public Integer getOrder() {
    return order;
  }
}
