package ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro;

public record Etiquette (String value) {

  public Boolean isEmpty() {
    return value == null || value.isEmpty();
  }
  
  public String toString() {
    return value;
  }
}
