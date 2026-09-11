package ca.mbjolin.editor.process._2_intermediaterepresentation.document;

import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Description;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Identifiant;

public record Source (Identifiant identifiant, Description description) {

  public Source(Identifiant identifiant, Description description) {
    //assert identifiant.isEmpty() ^ description.isEmpty();
    this.identifiant = identifiant;
    this.description = description;
  }
}
