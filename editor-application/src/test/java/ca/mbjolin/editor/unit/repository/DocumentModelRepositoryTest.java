package ca.mbjolin.editor.unit.repository;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Citation;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Contenu;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Document;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Meta;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Paragraphe;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Reglelogique;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Section;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Etiquette;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Identifiant;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Valeur;
import ca.mbjolin.editor.repository.DocumentModelRepository;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.ModeleRecord;

public class DocumentModelRepositoryTest {

  private static DocumentModelRepository repo;

  @BeforeAll
  public static void setup() {
    repo = new DocumentModelRepository(null, null);
  }

  @Test
  public void convertDocumentModel() {
    // GIVEN
    List<Meta> metas = new ArrayList<Meta>();
    metas.add(
        new Meta(new Identifiant("metaId"), new Etiquette("metaEtiquette"), new Valeur("valeur")));
    List<Section> sections = new ArrayList<Section>();

    Section section1 =
        new Section(new Identifiant("section1Id"), new Etiquette("section1Etiquette"),
            new ArrayList<Paragraphe>(), new ArrayList<Section>(), true);
    Section section2 =
        new Section(new Identifiant("section2Id"), new Etiquette("section2Etiquette"),
            new ArrayList<Paragraphe>(), new ArrayList<Section>(), false);

    Paragraphe para1 = new Paragraphe(new Identifiant("para1"), new ArrayList<Contenu>(),
        new ArrayList<Citation>(), new ArrayList<Reglelogique>());
    section2.paragraphes().add(para1);
    section1.sections().add(section2);

    Document document = new Document(new Identifiant("documentId"), new Identifiant("projetId"),
        metas, sections);
    document.sections().add(section1);
    document.sections().add(section2);

    // WHEN
    ModeleRecord model = repo.convertToModele(document);
    Document documentResult = repo.convertToDocument(model);

    // THEN
    Assertions.assertEquals(document.sections(), documentResult.sections());
    Assertions.assertEquals(document, documentResult);
  }
}
