package ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro;

import java.util.UUID;

public record Identifiant (String value) {
  public Identifiant {
    if (value == null) {
      value = UUID.randomUUID().toString();
    }
  }
  
  public Boolean isEmpty() {
    return value == null || value.isEmpty();
  }
  
  public String toString() {
    return value;
  }

}
