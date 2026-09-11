package ca.mbjolin.editor.process._2_intermediaterepresentation.regle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

public class RuleUtil {

  public static Document fillProjetWithMeta(Document doc, String etiquette) {
    Document newDoc = doc;
    for (Meta meta : doc.metas()) {
      if (meta.etiquette().value().equals(etiquette)) {
        newDoc = doc.withProjet(new Identifiant(meta.valeur().value()));
      }
    }
    return newDoc;
  }

  public static Boolean validProjetWithMeta(Document doc, String etiquette) {
    Boolean isValid = true;
    for (Meta meta : doc.metas()) {
      if (meta.etiquette().value().equals(etiquette)) {
        if (!meta.valeur().value().equals(doc.projet().value())) {
          isValid = false;
        }
      }
    }
    return isValid;
  }

  public static Document fillDocumentWithMeta(Document doc, String etiquette) {
    Document newDoc = doc;
    for (Meta meta : doc.metas()) {
      if (meta.etiquette().value().equals(etiquette)) {
        newDoc = doc.withDocumentId(new Identifiant(meta.valeur().value()));
      }
    }
    return newDoc;
  }

  public static Boolean validDocumentWithMeta(Document doc, String etiquette) {
    Boolean isValid = true;
    for (Meta meta : doc.metas()) {
      if (meta.etiquette().value().equals(etiquette)) {
        if (!meta.valeur().value().equals(doc.projet().value())) {
          isValid = false;
        }
      }
    }
    return isValid;
  }

  public static Document fillDocWithId(Document doc) {
    Document newDoc = doc;
    List<Meta> metas = new ArrayList<Meta>();
    for (Meta meta : doc.metas()) {
      Meta metaN;
      if (meta.identifiant().isEmpty()) {
        metaN = new Meta(new Identifiant(UUID.randomUUID().toString()),
            new Etiquette(meta.etiquette().value()), meta.valeur());
      } else {
        metaN = meta;
      }
      metas.add(metaN);
    }

    Map<String, Section> sectionMap = new HashMap<>();
    List<Section> sections = new ArrayList<>();
    for (Section section : doc.sections()) {
      List<Paragraphe> paras = new ArrayList<Paragraphe>();
      for (Paragraphe para : section.paragraphes()) {
        List<Contenu> contenus = new ArrayList<Contenu>();
        for (Contenu contenu : para.contenus()) {
          if (contenu.identifiant().isEmpty()) {
            contenus
                .add(new Contenu(new Identifiant(UUID.randomUUID().toString()),
                    contenu.description()));
          } else {
            contenus.add(contenu);
          }
        }
        List<Citation> citations = new ArrayList<Citation>();
        for (Citation citation : para.citations()) {
          List<Source> sources = new ArrayList<Source>();
          for (Source source : citation.sources()) {
            if (source.identifiant().isEmpty()) {
              sources
                  .add(new Source(new Identifiant(UUID.randomUUID().toString()),
                      source.description()));
            } else {
              sources.add(source);
            }
          }
          Citation cit;
          if (citation.identifiant().isEmpty()) {
            cit = new Citation(new Identifiant(UUID.randomUUID().toString()),
                citation.description(), sources);
          } else {
            cit = new Citation(citation.identifiant(),
                citation.description(), sources);
          }
          citations.add(cit);
        }

        List<Reglelogique> reglelogiques = new ArrayList<Reglelogique>();
        for (Reglelogique reglelogique : para.reglelogiquex()) {
          List<Source> sources = new ArrayList<Source>();
          for (Source source : reglelogique.sources()) {
            if (source.identifiant().isEmpty()) {
              sources
                  .add(new Source(new Identifiant(UUID.randomUUID().toString()),
                      source.description()));
            } else {
              sources.add(source);
            }
          }
          Reglelogique pred;
          if (reglelogique.identifiant().isEmpty()) {
            pred = new Reglelogique(new Identifiant(UUID.randomUUID().toString()),
                reglelogique.descriptionFormelle(), reglelogique.descriptionNaturelle(), sources);
          } else {
            pred = new Reglelogique(reglelogique.identifiant(),
                reglelogique.descriptionFormelle(), reglelogique.descriptionNaturelle(), sources);
          }
          reglelogiques.add(pred);
        }
        Paragraphe paraN;
        if (para.identifiant().isEmpty()) {
          paraN = new Paragraphe(new Identifiant(UUID.randomUUID().toString()),
              contenus, citations, reglelogiques);
        } else {
          paraN = new Paragraphe(para.identifiant(),
              contenus, citations, reglelogiques);
        }
        paras.add(paraN);
      }
      Section sectionN;
      if (section.identifiant().isEmpty()) {
        sectionN = new Section(new Identifiant(UUID.randomUUID().toString()),
            section.etiquette(), paras, section.sections(), section.racinaire());
      } else {
        sectionN = new Section(section.identifiant(),
            section.etiquette(), paras, section.sections(), section.racinaire());
      }
      sections.add(sectionN);
      sectionMap.put(sectionN.etiquette().value().toString(), sectionN);
    }

    List<Section> newSection = new ArrayList<>();
    for (Section section : sections) {
      List<Section> childs = new ArrayList<>();
      for (Section childSection : section.sections()) {
        childs.add(sectionMap.get(childSection.etiquette().value()));
      }
      Section sectionUpdate = new Section(section.identifiant(), section.etiquette(),
          section.paragraphes(), childs, section.racinaire());
      newSection.add(sectionUpdate);
    }

    newDoc = new Document(doc.identifiant(), doc.projet(), metas, newSection);
    return newDoc;
  }

  public static Document cleanId(Document doc) {
    List<Meta> metas = new ArrayList<Meta>();
    for (Meta meta : doc.metas()) {
      Meta metaN = new Meta(new Identifiant(meta.identifiant().value().replaceAll("^\\[|\\]$", "")),
          meta.etiquette(), meta.valeur());
      metas.add(metaN);
    }

    Map<String, Section> sectionMap = new HashMap<>();
    List<Section> sections = new ArrayList<>();
    for (Section section : doc.sections()) {
      String newEtiquette = section.etiquette().value().replace(section.identifiant().value(), "");
      String newId = section.identifiant().value().replaceAll("^\\[|\\]$", "");
      Section sectionN =
          new Section(new Identifiant(newId),
              new Etiquette(newEtiquette), section.paragraphes(), section.sections(), section.racinaire());
      sections.add(sectionN);
      sectionMap.put(sectionN.etiquette().value().toString(), sectionN);
    }

    List<Section> sectionNew = new ArrayList<>();
    for (Section section : sections) {
      List<Section> childs = new ArrayList<>();
      for (Section childSection : section.sections()) {
        String newEtiquette = childSection.etiquette().value().replace(childSection.identifiant().value(), "");
        childs.add(sectionMap.get(newEtiquette));
      }
      Section sectionUpdate = new Section(section.identifiant(), section.etiquette(),
          section.paragraphes(), childs, section.racinaire());
      sectionNew.add(sectionUpdate);
    }
    
    Document newDoc = new Document(doc.identifiant(), doc.projet(), metas, sectionNew);
    return newDoc;
  }

  public static Document stripContent(Document doc) {
    List<Meta> metas = new ArrayList<Meta>();
    for (Meta meta : doc.metas()) {
      Meta metaN = new Meta(meta.identifiant(),
          new Etiquette(strip(meta.etiquette().value())), new Valeur(strip(meta.valeur().value())));
      metas.add(metaN);
    }

    Map<String, Section> sectionMap = new HashMap<>();
    List<Section> sections = new ArrayList<>();
    for (Section section : doc.sections()) {
      List<Paragraphe> paras = new ArrayList<Paragraphe>();
      for (Paragraphe para : section.paragraphes()) {
        List<Contenu> contenus = new ArrayList<Contenu>();
        for (Contenu contenu : para.contenus()) {
            contenus.add(new Contenu(contenu.identifiant(),
                    new Description(strip(contenu.description().value()))));
        }
        List<Citation> citations = new ArrayList<Citation>();
        for (Citation citation : para.citations()) {
          List<Source> sources = new ArrayList<Source>();
          for (Source source : citation.sources()) {
            sources
                .add(new Source(source.identifiant(),
                    new Description(strip(source.description().value()))));
          }
          Citation cit = new Citation(citation.identifiant(),
              new Description(strip(citation.description().value())), sources);
          citations.add(cit);
        }

        List<Reglelogique> reglelogiques = new ArrayList<Reglelogique>();
        for (Reglelogique reglelogique : para.reglelogiquex()) {
          List<Source> sources = new ArrayList<Source>();
          for (Source source : reglelogique.sources()) {
            sources
            .add(new Source(source.identifiant(),
                new Description(strip(source.description().value()))));
          }
          Reglelogique pred = new Reglelogique(reglelogique.identifiant(),
              new Description(strip(reglelogique.descriptionFormelle().value())), 
              new Description(strip(reglelogique.descriptionNaturelle().value())), sources);
          reglelogiques.add(pred);
        }
        Paragraphe  paraN = new Paragraphe(para.identifiant(),
            contenus, citations, reglelogiques);
        paras.add(paraN);
      }
      Section sectionN = new Section(new Identifiant(strip(section.identifiant().value())),
          new Etiquette(strip(section.etiquette().value())), section.paragraphes(), section.sections(), section.racinaire());

      sections.add(sectionN);
      sectionMap.put(sectionN.etiquette().value().toString(), sectionN);
    }

    List<Section> newSection = new ArrayList<>();
    for (Section section : sections) {
      List<Section> childs = new ArrayList<>();
      for (Section childSection : section.sections()) {
        childs.add(sectionMap.get(strip(childSection.etiquette().value())));
      }
      Section sectionUpdate = new Section(section.identifiant(), section.etiquette(),
          section.paragraphes(), childs, section.racinaire());
      newSection.add(sectionUpdate);
    }

    Document newDoc = new Document(doc.identifiant(), doc.projet(), metas, newSection);
    return newDoc;
  }
  
  private static String strip(String content) {
    String newContent = "";
    if (content != null) {
      newContent = content.strip();
    }
    return newContent;
  }

}
