package ca.mbjolin.editor.dao._2_document;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;

import ca.mbjolin.gen.editor_ldm.document_api.Routines;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.DocumentRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DocumentDao {

  private final Configuration config;

  @Inject
  public DocumentDao(Configuration config) {
    this.config = config;
  }

  public List<DocumentRecord> search(String query) {
    DocumentRecord[] result = Routines.searchDocument(config, query);
    return Arrays.asList(result);
  }
  
  public List<DocumentRecord> getAll() {
    DocumentRecord[] result = Routines.getallDocument(config);
    return Arrays.asList(result);
  }

  public Optional<DocumentRecord> create(DocumentRecord document) {
    return Optional.of(Routines.createDocument(config, document));
  }

  public Optional<DocumentRecord> read(DocumentRecord document) {
    return Optional.of(Routines.readDocument(config, document));
  }

  public Optional<DocumentRecord> update(DocumentRecord newDocument, DocumentRecord oldDocument) {
    DocumentRecord result = null;
    result = Routines.updateDocument(config, newDocument, oldDocument);
    return Optional.of(result);
  }

  public void delete(DocumentRecord document) {
    Routines.deleteDocument(config, document);
  }
}
