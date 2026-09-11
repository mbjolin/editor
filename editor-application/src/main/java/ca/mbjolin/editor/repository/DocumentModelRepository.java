package ca.mbjolin.editor.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jooq.Configuration;
import org.slf4j.event.Level;

import ca.mbjolin.editor.journal.Journal;
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
import ca.mbjolin.gen.editor_ldm.document_api.Routines;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.CitationRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.CitationSourceRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.ContenuRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.DocumentMetaRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.DocumentRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.DocumentSectionRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.MetaRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.ModeleRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.ObjetRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.ParagrapheCitationRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.ParagrapheContenuRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.ParagrapheRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.ParagrapheReglelogiqueRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.ReglelogiqueRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.ReglelogiqueSourceRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.RelationRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.SectionParagrapheRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.SectionRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.SectionSectionRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.SourceRecord;

/**
 * MR modèle relationnel.
 * MO modèle objet appelé modèle intermédiaire.
 * 
 * Chacune des relations et des tables du MR sont représentées par des type postgresql.
 * Pour chacun des types, JOOQ crée des Records.
 * Le ModeleRecord contient l'ensemble des relations et des tables du MR.
 * 
 * Dans cette classe, on convertit le MO vers le ModeleRecord et vice versa.
 * Il y a des procédures pour convertir le ModeleRecord vers le MR et vice versa.
 * 
 * Cas 1 : S'il y a des changements au MR, sans régénérer les types cela peut continuer à
 * fonctionner. Voir UserDaoTest.java (Si on prend les types générés par postgresql, il pourrait
 * peut-être y avoir des erreurs de déclenchés.)
 * 
 * Cas 2 : S'il y a des changements au MR, qu'on régénère les types et qu'on régénère jooq cela va
 * cesser de compiler.
 * 
 * Cas 3 : S'il y a des changements au MO, cela va cesser de compiler.
 * 
 * Note : On ne peut pas facilement régénérer les types sans régénérer les interfaces qui les
 * utilisent.
 * 
 */

public class DocumentModelRepository {

  private final Configuration configuration;

  private Journal journal;

  public DocumentModelRepository(Configuration configuration, Journal journal) {
    this.configuration = configuration;
    this.journal = journal;
  }

  public Optional<Document> getModele(Document document, Boolean withError) {
    Document newDocument = null;

    try {
      ModeleRecord modele = Routines.getModele(configuration, document.identifiant().value());
      newDocument = convertToDocument(modele);
    } catch (Exception e) {
      if (withError) {
        journal.add(Level.ERROR, "", e.getMessage());
      }
    }

    return Optional.ofNullable(newDocument);
  }
  
  public Optional<ModeleRecord> getModeleRaw(Document document, Boolean withError) {
    ModeleRecord modele = null;
    try {
      modele = Routines.getModele(configuration, document.identifiant().value());
    } catch (Exception e) {
      if (withError) {
        journal.add(Level.ERROR, "", e.getMessage());
      }
    }
    return Optional.ofNullable(modele);
  }
  
  public Boolean modeleExist(Document document) {
    Boolean isExist = false;
    DocumentRecord[] result;
    try {
      result = Routines.getwithidsDocument(configuration, new String[] {document.identifiant().value()});
      if(result.length != 0) {
        isExist = true;
      }
    } catch (Exception e) {
      journal.add(Level.ERROR, "", e.getMessage());
    }
    return isExist;
  }

  public Optional<Document> createModele(Document document) {
    Document newDocument = null;
    try {
      ModeleRecord modele = convertToModele(document);
      Routines.createModele(configuration, modele);
      ModeleRecord newModele = Routines.getModele(configuration, document.identifiant().value());
      newDocument = convertToDocument(newModele);
    } catch (Exception e) {
      journal.add(Level.ERROR, "", e.getMessage());
    }

    return Optional.ofNullable(newDocument);
  }

  public Boolean verifyModele(Document document) {
    Boolean isExist = modeleExist(document);
    Boolean isVerify = true;
    ModeleRecord modele = convertToModele(document);
    if (!isExist) {
      try {
        Routines.verifyCreationModele(configuration, modele);
      } catch (Exception e) {
        journal.add(Level.ERROR, e.getMessage());
        isVerify = false;
      }
    } else {
      
      try {
        Optional<ModeleRecord> modeleOld = getModeleRaw(document, false);
        Routines.verifyModificationModele(configuration, modele, modeleOld.get());
      } catch (Exception e) {
        journal.add(Level.ERROR, e.getMessage());
        isVerify = false;
      }
    }

    return isVerify;
  }

  public Document updateModele(Document newdocument, Document olddocument) {
    Document resultDocument = null;
    ModeleRecord newmodele = convertToModele(newdocument);
    ModeleRecord oldmodele = convertToModele(olddocument);
    try {
      Routines.updateModele(configuration, newmodele, oldmodele);
      ModeleRecord resultmodele =
          Routines.getModele(configuration, newdocument.identifiant().value());
      resultDocument = convertToDocument(resultmodele);
    } catch (Exception e) {
      journal.add(Level.ERROR, e.getMessage());
    }
    return resultDocument;
  }

  public Journal getJournal() {
    return journal;
  }

  public Document convertToDocument(ModeleRecord modele) {

    ObjetRecord objet = modele.getObjet();

    Map<String, Source> sources = new HashMap<>();
    for (SourceRecord sou : objet.getSources()) {
      sources.put(sou.getIdSource(),
          new Source(new Identifiant(sou.getIdSource()), new Description(sou.getDescription())));
    }

    Map<String, Reglelogique> reglelogiques = new HashMap<>();
    for (ReglelogiqueRecord pre : objet.getReglelogiques()) {
      reglelogiques.put(pre.getIdReglelogique(),
          new Reglelogique(new Identifiant(pre.getIdReglelogique()),
              new Description(pre.getDescriptionFormelle()),
              new Description(pre.getDescriptionNaturelle()),
              new ArrayList<Source>()));
    }

    Map<String, Citation> citations = new HashMap<>();
    for (CitationRecord cit : objet.getCitations()) {
      citations.put(cit.getIdCitation(),
          new Citation(new Identifiant(cit.getIdCitation()),
              new Description(cit.getDescription()),
              new ArrayList<Source>()));
    }

    Map<String, Contenu> contenus = new HashMap<>();
    for (ContenuRecord con : objet.getContenus()) {
      contenus.put(con.getIdContenu(),
          new Contenu(new Identifiant(con.getIdContenu()),
              new Description(con.getDescription())));
    }

    Map<String, Paragraphe> paragraphes = new HashMap<>();
    for (ParagrapheRecord par : objet.getParagraphes()) {
      paragraphes.put(par.getIdParagraphe(),
          new Paragraphe(new Identifiant(par.getIdParagraphe()),
              new ArrayList<Contenu>(),
              new ArrayList<Citation>(),
              new ArrayList<Reglelogique>()));
    }

    /* ici */
    Map<String, Section> sections = new HashMap<>();
    for (SectionRecord sec : objet.getSections()) {
      sections.put(sec.getIdSection(),
          new Section(new Identifiant(sec.getIdSection()), new Etiquette(sec.getEtiquette()),
              new ArrayList<Paragraphe>(),
              new ArrayList<Section>(), sec.getRacinaire()));
    }

    Map<String, Meta> metas = new HashMap<>();
    for (MetaRecord met : objet.getMetas()) {
      metas.put(met.getIdMeta(),
          new Meta(new Identifiant(met.getIdMeta()), new Etiquette(met.getEtiquette()),
              new Valeur(met.getValeur())));
    }

    Map<String, Document> documents = new HashMap<>();
    for (DocumentRecord doc : objet.getDocuments()) {
      documents.put(doc.getIdDocument(),
          new Document(new Identifiant(doc.getIdDocument()),
              new Identifiant(doc.getIdProjet()),
              new ArrayList<Meta>(),
              new ArrayList<Section>()));
    }

    RelationRecord relation = modele.getRelation();

    for (DocumentMetaRecord rel : relation.getDocumentMeta()) {
      Document doc = documents.get(rel.getIdDocument());
      Meta meta = metas.get(rel.getIdMeta());
      doc.metas().add(meta);
    }

    for (DocumentSectionRecord rel : relation.getDocumentSection()) {
      Document doc = documents.get(rel.getIdDocument());
      Section section = sections.get(rel.getIdSection());
      doc.sections().add(section);
    }

    for (SectionParagrapheRecord rel : relation.getSectionParagraphe()) {
      Section section = sections.get(rel.getIdSection());
      Paragraphe para = paragraphes.get(rel.getIdParagraphe());
      section.paragraphes().add(para);
    }

    for (SectionSectionRecord rel : relation.getSectionSection()) {
      Section section = sections.get(rel.getIdSection());
      Section sectionChild = sections.get(rel.getIdEnfant());
      section.sections().add(sectionChild);
    }

    for (ParagrapheContenuRecord rel : relation.getParagrapheContenu()) {
      Paragraphe para = paragraphes.get(rel.getIdParagraphe());
      Contenu contenu = contenus.get(rel.getIdContenu());
      para.contenus().add(contenu);
    }

    for (ParagrapheCitationRecord rel : relation.getParagrapheCitation()) {
      Paragraphe para = paragraphes.get(rel.getIdParagraphe());
      Citation citation = citations.get(rel.getIdCitation());
      para.citations().add(citation);
    }

    for (ParagrapheReglelogiqueRecord rel : relation.getParagrapheReglelogique()) {
      Paragraphe para = paragraphes.get(rel.getIdParagraphe());
      Reglelogique reglelogique = reglelogiques.get(rel.getIdReglelogique());
      para.reglelogiquex().add(reglelogique);
    }

    for (CitationSourceRecord rel : relation.getCitationSource()) {
      Citation citation = citations.get(rel.getIdCitation());
      Source source = sources.get(rel.getIdSource());
      citation.sources().add(source);
    }

    for (ReglelogiqueSourceRecord rel : relation.getReglelogiqueSource()) {
      Reglelogique reglelogique = reglelogiques.get(rel.getIdReglelogique());
      Source source = sources.get(rel.getIdSource());
      reglelogique.sources().add(source);
    }

    Document document;
    if (documents.values().size() != 1) {
      document = new Document(null, null, null, null);
    } else {
      document = documents.values().iterator().next();
    }

    return document;
  }

  public ModeleRecord convertToModele(Document document) {
    ModeleRecord modele = new ModeleRecord();

    List<DocumentRecord> docs = new ArrayList<>();
    DocumentRecord doc = new DocumentRecord();
    doc.setIdDocument(document.identifiant().value());
    doc.setIdProjet(document.projet().value());
    docs.add(doc);

    List<MetaRecord> metas = new ArrayList<>();
    List<DocumentMetaRecord> documentmeta = new ArrayList<>();
    for (Meta meta : document.metas()) {
      MetaRecord obj = new MetaRecord();
      obj.setIdMeta(meta.identifiant().value());
      obj.setEtiquette(meta.etiquette().value());
      obj.setValeur(meta.valeur().value());
      metas.add(obj);

      DocumentMetaRecord rel = new DocumentMetaRecord();
      rel.setIdDocument(document.identifiant().value());
      rel.setIdMeta(meta.identifiant().value());
      documentmeta.add(rel);
    }

    List<SectionRecord> sections = new ArrayList<>();
    List<DocumentSectionRecord> documentsection = new ArrayList<>();

    for (Section section : document.sections()) {
      SectionRecord obj = new SectionRecord();
      obj.setIdSection(section.identifiant().value());
      obj.setEtiquette(section.etiquette().value());
      obj.setRacinaire(section.racinaire());
      sections.add(obj);

      DocumentSectionRecord rel = new DocumentSectionRecord();
      rel.setIdDocument(document.identifiant().value());
      rel.setIdSection(section.identifiant().value());
      documentsection.add(rel);
    }

    List<SectionSectionRecord> sectionsection = new ArrayList<>();
    for (Section section : document.sections()) {
      findSectionRelation(section, sectionsection);
    }

    List<SectionParagrapheRecord> sectionparagraphe = new ArrayList<>();
    List<ParagrapheRecord> paragraphes = new ArrayList<>();
    List<Paragraphe> paragrapheInDoc = new ArrayList<>();
    for (Section section : document.sections()) {
      for(Paragraphe paragraphe : section.paragraphes()) {
        paragrapheInDoc.add(paragraphe);
        ParagrapheRecord obj = new ParagrapheRecord();
        obj.setIdParagraphe(paragraphe.identifiant().value());
        paragraphes.add(obj);

        SectionParagrapheRecord sectionpara = new SectionParagrapheRecord();
        sectionpara.setIdSection(section.identifiant().value());
        sectionpara.setIdParagraphe(paragraphe.identifiant().value());
        sectionparagraphe.add(sectionpara);
      }
    }

    List<ContenuRecord> contenus = new ArrayList<>();
    List<ParagrapheContenuRecord> paragraphecontenu = new ArrayList<>();
    List<CitationRecord> citations = new ArrayList<>();
    List<ParagrapheCitationRecord> paragraphecitation = new ArrayList<>();
    List<ReglelogiqueRecord> reglelogiques = new ArrayList<>();
    List<ParagrapheReglelogiqueRecord> paragraphereglelogique = new ArrayList<>();
    List<SourceRecord> sources = new ArrayList<>();
    List<CitationSourceRecord> citationsource = new ArrayList<>();
    List<ReglelogiqueSourceRecord> reglelogiquesource = new ArrayList<>();
    for(Paragraphe paragraphe : paragrapheInDoc) {
      for (Contenu contenu : paragraphe.contenus()) {
        ContenuRecord con = new ContenuRecord();
        con.setIdContenu(contenu.identifiant().value());
        con.setDescription(contenu.description().value());
        contenus.add(con);

        ParagrapheContenuRecord rel = new ParagrapheContenuRecord();
        rel.setIdParagraphe(paragraphe.identifiant().value());
        rel.setIdContenu(contenu.identifiant().value());
        paragraphecontenu.add(rel);
      }

      for (Citation citation : paragraphe.citations()) {
        CitationRecord cit = new CitationRecord();
        cit.setIdCitation(citation.identifiant().value());
        cit.setDescription(citation.description().value());
        citations.add(cit);

        ParagrapheCitationRecord rel = new ParagrapheCitationRecord();
        rel.setIdParagraphe(paragraphe.identifiant().value());
        rel.setIdCitation(citation.identifiant().value());
        paragraphecitation.add(rel);

        for (Source source : citation.sources()) {
          SourceRecord sou = new SourceRecord();
          sou.setIdSource(source.identifiant().value());
          sou.setDescription(source.description().value());
          sources.add(sou);

          CitationSourceRecord citsou = new CitationSourceRecord();
          citsou.setIdCitation(citation.identifiant().value());
          citsou.setIdSource(source.identifiant().value());
          citationsource.add(citsou);
        }
      }

      for (Reglelogique reglelogique : paragraphe.reglelogiquex()) {
        ReglelogiqueRecord pre = new ReglelogiqueRecord();
        pre.setIdReglelogique(reglelogique.identifiant().value());
        pre.setDescriptionFormelle(reglelogique.descriptionFormelle().value());
        pre.setDescriptionNaturelle(reglelogique.descriptionNaturelle().value());
        reglelogiques.add(pre);

        ParagrapheReglelogiqueRecord rel = new ParagrapheReglelogiqueRecord();
        rel.setIdParagraphe(paragraphe.identifiant().value());
        rel.setIdReglelogique(reglelogique.identifiant().value());
        paragraphereglelogique.add(rel);

        for (Source source : reglelogique.sources()) {
          SourceRecord sou = new SourceRecord();
          sou.setIdSource(source.identifiant().value());
          sou.setDescription(source.description().value());
          sources.add(sou);

          ReglelogiqueSourceRecord presou = new ReglelogiqueSourceRecord();
          presou.setIdReglelogique(reglelogique.identifiant().value());
          presou.setIdSource(source.identifiant().value());
          reglelogiquesource.add(presou);
        }
      }
    }

    ObjetRecord objet = new ObjetRecord();
    objet.setDocuments(docs.toArray(new DocumentRecord[0]));
    objet.setMetas(metas.toArray(new MetaRecord[0]));
    objet.setSections(sections.toArray(new SectionRecord[0]));
    objet.setParagraphes(paragraphes.toArray(new ParagrapheRecord[0]));
    objet.setContenus(contenus.toArray(new ContenuRecord[0]));
    objet.setCitations(citations.toArray(new CitationRecord[0]));
    objet.setReglelogiques(reglelogiques.toArray(new ReglelogiqueRecord[0]));
    objet.setSources(sources.toArray(new SourceRecord[0]));

    RelationRecord relation = new RelationRecord();
    relation.setDocumentMeta(documentmeta.toArray(new DocumentMetaRecord[0]));
    relation.setDocumentSection(documentsection.toArray(new DocumentSectionRecord[0]));
    relation.setSectionParagraphe(sectionparagraphe.toArray(new SectionParagrapheRecord[0]));
    relation.setSectionSection(sectionsection.toArray(new SectionSectionRecord[0]));
    relation.setParagrapheContenu(paragraphecontenu.toArray(new ParagrapheContenuRecord[0]));
    relation.setParagrapheCitation(paragraphecitation.toArray(new ParagrapheCitationRecord[0]));
    relation.setParagrapheReglelogique(paragraphereglelogique.toArray(new ParagrapheReglelogiqueRecord[0]));
    relation.setCitationSource(citationsource.toArray(new CitationSourceRecord[0]));
    relation.setReglelogiqueSource(reglelogiquesource.toArray(new ReglelogiqueSourceRecord[0]));

    modele.setObjet(objet);
    modele.setRelation(relation);
    return modele;
  }

  private void findSectionRelation(Section section, List<SectionSectionRecord> sectionsection) {
    for (Section sectionChild : section.sections()) {
      SectionSectionRecord rel = new SectionSectionRecord();
      rel.setIdSection(section.identifiant().value());
      rel.setIdEnfant(sectionChild.identifiant().value());
      sectionsection.add(rel);
      findSectionRelation(sectionChild, sectionsection);
    }
  }

}
