package ca.mbjolin.editor.repository;

import org.jooq.Configuration;
import org.slf4j.event.Level;

import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Document;
import ca.mbjolin.gen.editor_ldm.document_api.Routines;

public class ConceptRepository {

  private final Configuration configuration;

  private Journal journal;

  public ConceptRepository(Configuration configuration, Journal journal) {
    this.configuration = configuration;
    this.journal = journal;
  }

  public void syncDocAndConcept(Document newdocument, Document olddocument) {
    Document resultDocument = null;
    
    /*Get all concept */
    ca.mbjolin.gen.editor_ldm.concevoir_api.Routines.getallConcept(configuration);
    
    /* find all concept in content and create list */
    
    /* MBJ */
    /* find all relation concept and create legacy list */
    //Routines.getReglelogique
    
    try {
      Routines.deleteContenuConcept(configuration, null);
      Routines.deleteCitationConcept(configuration, null);
      Routines.deleteReglelogiqueConcept(configuration, null);
      Routines.deleteSourceConcept(configuration, null);
      
      Routines.createContenuConcept(configuration, null);
      Routines.createCitationConcept(configuration, null);
      Routines.createReglelogiqueConcept(configuration, null);
      Routines.createSourceConcept(configuration, null);
      //ModeleRecord modele = Routines.getAllModele(configuration, document.identifiant().value());
      //newDocument = convertToDocument(modele);
    } catch (Exception e) {
        journal.add(Level.ERROR, "", e.getMessage());
    }
  }
}
