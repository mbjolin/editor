package ca.mbjolin.editor.process._2_intermediaterepresentation.document;

import java.util.List;

import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Identifiant;

public record Paragraphe (
    Identifiant identifiant,
    List<Contenu> contenus, List<Citation> citations, List<Reglelogique> reglelogiquex) {

    public Paragraphe(Identifiant identifiant, List<Contenu> contenus,
      List<Citation> citations, List<Reglelogique>  reglelogiquex) {
    this.identifiant = identifiant;
    this.contenus = contenus;
    this.citations = citations;
    this.reglelogiquex = reglelogiquex;
  }
}
