package ca.mbjolin.editor.process._3_presentation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.event.Level;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupString;

import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Citation;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Contenu;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Document;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Meta;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Paragraphe;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Reglelogique;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Section;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Source;
import ca.mbjolin.editor.util.CustomSTErrorListener;

public class PresentationProcessing {

  private Document document;

  private STGroup group;

  private String out;

  private Journal journal;

  public PresentationProcessing(String name, Journal journal, Boolean isName) {
    if (isName) {
      String groupString = findTemplate(name);
      initTemplate(groupString);

    } else {
      initTemplate(name);
    }

    this.journal = journal;
  }

  public void execute(Document doc) {

    verifyTemplateName();
    try {
      out = convertDoc(doc);
    } catch (Exception e) {
      journal.add(Level.ERROR, "Error lors de la convertion.", e.getMessage());
    }

  }

  public String findTemplate(String name) {
    String content = "";
    InputStream is = PresentationProcessing.class.getClassLoader()
        .getResourceAsStream("antlr/template/"+ name + ".stg");
    if (is == null) {
      journal.add(Level.ERROR, "Il manque ce template : ", name + ".stg");
    } else {
      try {
        content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
      } catch (IOException e) {
        journal.add(Level.ERROR, e.getMessage());
      } ;
    }
    return content;
  }

  public void initTemplate(String groupString) {
    group = new STGroupString(groupString);
    group.setListener(new CustomSTErrorListener(journal));
  }

  private void verifyTemplateName() {
    Set<String> names = group.getTemplateNames();
    List<String> mandatoryNames = new ArrayList<>();
    mandatoryNames.add("/doc");
    mandatoryNames.add("/meta");
    mandatoryNames.add("/section");
    mandatoryNames.add("/paragraphe");
    mandatoryNames.add("/contenu");
    mandatoryNames.add("/reglelogiqueF");
    mandatoryNames.add("/reglelogiqueN");
    mandatoryNames.add("/citation");
    mandatoryNames.add("/source");

    for (String mandatoryName : mandatoryNames) {
      if (!names.contains(mandatoryName)) {
        journal.add(Level.ERROR, "Il manque ce template.", mandatoryName);
      }
    }
  }

  public String convertMetas(List<Meta> metas) {
    String result = "";
    if (metas != null) {
      for (Meta meta : metas) {
        result = result.concat(convertMeta(meta));
      }
    }
    return result;
  }

  public String convertMeta(Meta meta) {
    ST template = group.getInstanceOf("meta");
    template.add("identifiant", meta.identifiant().value());
    template.add("etiquette", meta.etiquette().value());
    template.add("valeur", meta.valeur().value());
    return template.render();
  }

  public String convertSectionsRacine(List<Section> sections, Integer depth) {
    String result = "";
    if (sections != null) {
      for (Section section : sections) {
        if(section.racinaire()) {
          result = result.concat(convertSection(section, depth));
        }
      }
    }
    return result;
  }
  
  public String convertSections(List<Section> sections, Integer depth) {
    String result = "";
    if (sections != null) {
      for (Section section : sections) {
        result = result.concat(convertSection(section, depth));
      }
    }
    return result;
  }

  public String convertSection(Section section, Integer depth) {
    ST template = group.getInstanceOf("section");
    template.add("identifiant", section.identifiant().value());
    template.add("etiquette", section.etiquette().value());
    template.add("profondeur", depth);
    template.add("paragraphes", convertParagraphes(section.paragraphes()));
    template.add("sections", convertSections(section.sections(), depth + 1));
    return template.render();
  }

  public String convertParagraphes(List<Paragraphe> paragraphes) {
    String result = "";
    if (paragraphes != null) {
      for (Paragraphe para : paragraphes) {
        result = result.concat(convertParagraphe(para));
      }
    }
    return result;
  }

  public String convertParagraphe(Paragraphe paragraphe) {
    ST template = group.getInstanceOf("paragraphe");
    template.add("contenus", convertContenus(paragraphe.contenus()));
    template.add("reglelogiquex", convertReglelogiquex(paragraphe.reglelogiquex()));
    template.add("citations", convertCitations(paragraphe.citations()));
    return template.render();
  }

  public String convertContenus(List<Contenu> contenus) {
    String result = "";
    if (contenus != null) {
      for (Contenu contenu : contenus) {
        result = result.concat(convertContenu(contenu));
      }
    }
    return result;
  }

  public String convertContenu(Contenu contenu) {
    ST template = group.getInstanceOf("contenu");
    template.add("identifiant", contenu.identifiant().value());
    template.add("description", contenu.description().value());
    return template.render();
  }

  public String convertReglelogiquex(List<Reglelogique> reglelogiquex) {
    String result = "";
    if (reglelogiquex != null) {
      for (Reglelogique reglelogique : reglelogiquex) {
        result = result.concat(convertReglelogique(reglelogique));
      }
    }
    return result;
  }

  public String convertReglelogique(Reglelogique reglelogique) {
    ST template = group.getInstanceOf("reglelogiqueF");
    if(!reglelogique.descriptionFormelle().isEmpty()){
      template.add("identifiant", reglelogique.identifiant().value());
      template.add("descriptionFormelle", reglelogique.descriptionFormelle().value());
      template.add("sources", reglelogique.sources());
    } else if (!reglelogique.descriptionNaturelle().isEmpty()){
        template = group.getInstanceOf("reglelogiqueN");
        template.add("identifiant", reglelogique.identifiant().value());
        template.add("descriptionNaturelle", reglelogique.descriptionNaturelle().value());
        template.add("sources", reglelogique.sources());
    }

    return template.render();
  }

  public String convertCitations(List<Citation> citations) {
    String result = "";
    if (citations != null) {
      for (Citation citation : citations) {
        result = result.concat(convertCitation(citation));
      }
    }
    return result;
  }

  public String convertCitation(Citation citation) {
    ST template = group.getInstanceOf("citation");
    template.add("identifiant", citation.identifiant().value());
    template.add("description", citation.description().value());
    template.add("sources", convertSources(citation.sources()));

    return template.render();
  }

  public String convertSources(List<Source> sources) {
    String result = "";
    if (sources != null) {
      for (Source source : sources) {
        result = result.concat(convertSource(source));
      }
    }
    return result;
  }

  public String convertSource(Source source) {
    ST template = group.getInstanceOf("source");
    template.add("identifiant", source.identifiant().value());
    template.add("description", source.description().value());
    return template.render();
  }

  public String convertDoc(Document doc) {
    ST template = group.getInstanceOf("doc");
    template.add("identifiant", doc.identifiant().value());
    template.add("metas", convertMetas(doc.metas()));
    template.add("sections", convertSectionsRacine(doc.sections(), 0));
    return template.render();
  }

  public Journal getJournal() {
    return journal;
  }

  public String getContent() {
    return document.toString();
  }

  public Document getDocument() {
    return document;
  }

  public void setDocument(Document document) {
    this.document = document;
  }

  public String getOut() {
    return out;
  }

  public void setOut(String out) {
    this.out = out;

  }

}
