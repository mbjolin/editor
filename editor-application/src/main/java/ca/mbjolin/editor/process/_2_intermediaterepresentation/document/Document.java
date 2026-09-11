package ca.mbjolin.editor.process._2_intermediaterepresentation.document;

import java.util.List;

import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Identifiant;

public record Document (Identifiant identifiant, 
    Identifiant projet,
    List<Meta> metas, 
    List<Section> sections) {
  
  public Document withDocumentId(Identifiant documentId) {
    return new Document(documentId, projet, metas(), sections());
  }

  public Document withProjet(Identifiant projetId) {
    return new Document(identifiant, projetId, metas(), sections());
  }

  public Document withProjet(List<Section> newSections) {
    return new Document(identifiant, projet, metas(), newSections);
  }
}
