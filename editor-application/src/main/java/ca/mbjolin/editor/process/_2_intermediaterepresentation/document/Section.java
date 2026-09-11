package ca.mbjolin.editor.process._2_intermediaterepresentation.document;

import java.util.List;

import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Etiquette;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Identifiant;

public record Section (Identifiant identifiant, Etiquette etiquette, List<Paragraphe> paragraphes,
    List<Section> sections, Boolean racinaire) {
  public Section(Identifiant identifiant, Etiquette etiquette, List<Paragraphe> paragraphes,
      List<Section> sections, Boolean racinaire) {
    this.identifiant = identifiant;
    this.etiquette = etiquette;
    this.paragraphes = paragraphes;
    this.sections = sections;
    this.racinaire = racinaire;
  }

  public Section withRacinaire(Boolean racinaire) {
    return new Section(identifiant, etiquette, paragraphes, sections, racinaire);
  }

  public void Assert() {
    // assert paragraphes.isEmpty() ^ sections.isEmpty();
  }
}
