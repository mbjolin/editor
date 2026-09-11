package ca.mbjolin.editor.unit.process._2_intermediaterepresentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.process._2_intermediaterepresentation.LogicSystemProcessing;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Citation;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Contenu;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Document;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Meta;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Paragraphe;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Reglelogique;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Section;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Source;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Description;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Etiquette;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Identifiant;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Valeur;

/*
 * Inspirée de :
 * https://github.com/AlloyTools/models/tree/master/simple-models
 * */
public class LogicSystemProcessingTest {

  @Test
  public void executeBaseTest() throws IOException, InterruptedException {
    // GIVEN
    LogicSystemProcessing systemF = new LogicSystemProcessing(new Journal());

    // WHEN
    Boolean result = systemF.execute(createDocument());

    // THEN
    Journal jou = systemF.getJournal();

    System.out.println(jou.getMessagesAsString());
    Assertions.assertTrue(jou.isWithoutError());
    Assertions.assertTrue(result);
  }
  
  @Test
  public void executeFalseResultTest() throws IOException, InterruptedException {
    // GIVEN
    LogicSystemProcessing systemF = new LogicSystemProcessing(new Journal());

    // WHEN
    Boolean result = systemF.execute(createDocument2());

    // THEN
    Journal jou = systemF.getJournal();

    System.out.println(jou.getMessagesAsString());
    Assertions.assertTrue(jou.isWithoutError());
    Assertions.assertFalse(result);
  }

  private Document createDocument() {
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

    List<Reglelogique> rules = new ArrayList<Reglelogique>();
    rules.add(new Reglelogique(new Identifiant("rule1"), new Description("sig Point {}"), null,
        new ArrayList<Source>()));
    rules.add(new Reglelogique(new Identifiant("rule2"), new Description("run { #Point > 1 } for 3 but 3 Int"), null,
        new ArrayList<Source>()));
    Paragraphe para1 = new Paragraphe(new Identifiant("para1"), new ArrayList<Contenu>(),
        new ArrayList<Citation>(), rules);
    section2.paragraphes().add(para1);
    section1.sections().add(section2);

    Document document = new Document(new Identifiant("documentId"), new Identifiant("projetId"),
        metas, sections);
    document.sections().add(section1);
    document.sections().add(section2);
    return document;
  }

  private Document createDocument2() {
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

    List<Reglelogique> rules = new ArrayList<Reglelogique>();
    rules.add(new Reglelogique(new Identifiant("rule1"), new Description("sig S {}"), null,
        new ArrayList<Source>()));
    rules.add(new Reglelogique(new Identifiant("rule2"), new Description("fact { 1=2 }"), null,
        new ArrayList<Source>()));
    Paragraphe para1 = new Paragraphe(new Identifiant("para1"), new ArrayList<Contenu>(),
        new ArrayList<Citation>(), rules);
    section2.paragraphes().add(para1);
    section1.sections().add(section2);

    Document document = new Document(new Identifiant("documentId"), new Identifiant("projetId"),
        metas, sections);
    document.sections().add(section1);
    document.sections().add(section2);
    return document;
  }
}
