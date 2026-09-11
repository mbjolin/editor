package ca.mbjolin.editor.process._2_intermediaterepresentation.document;

import java.util.List;

import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Description;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Identifiant;

public record Reglelogique (Identifiant identifiant, Description descriptionFormelle,
    Description descriptionNaturelle, List<Source> sources) {

  public String getDescriptionN() {
    String val = "";
    if(descriptionNaturelle() != null && descriptionNaturelle().value() != null) {
      val = descriptionNaturelle().value();
    }
    return val;
  }
}
