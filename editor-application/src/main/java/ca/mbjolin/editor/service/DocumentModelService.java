package ca.mbjolin.editor.service;

import org.jooq.Configuration;

import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Document;
import ca.mbjolin.editor.repository.DocumentModelRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DocumentModelService {

  private DocumentModelRepository documentModelRepository;

  private Configuration configDb;
  
  private Journal journal;

  public DocumentModelService( Configuration configDb) {
    this.configDb = configDb;
    this.journal = new Journal();
    this.documentModelRepository = new DocumentModelRepository(configDb, journal);
  }

  public Document read(Document document) {
    return documentModelRepository.getModele(document,true).orElse(document);
  }

  public Document save(Document document) {
    return documentModelRepository.createModele(document).get();
  }

  public Document update(Document newDocument, Document oldDocument) {
    return documentModelRepository.updateModele(newDocument, oldDocument);
  }
}
