package ca.mbjolin.editor.process._2_intermediaterepresentation;

import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Document;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Paragraphe;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Section;

public class IntermediateRepresentationStat {
  
  Integer metaNumber = 0;
  Integer citationNumber = 0;
  Integer reglelogiqueNumber = 0;
  Integer paragraphNumber = 0;

  public IntermediateRepresentationStat(Document document) {
    createStat(document);
  }
  
  private void createStat(Document document) {
    metaNumber = document.metas().size();
    
    for(Section section : document.sections()) {
      paragraphNumber = paragraphNumber + section.paragraphes().size();
      
      for(Paragraphe paragraphe : section.paragraphes()) {
        citationNumber = citationNumber + paragraphe.citations().size();
        reglelogiqueNumber = reglelogiqueNumber + paragraphe.reglelogiquex().size();
      }
    }
  }

  public Integer getMetaNumber() {
    return metaNumber;
  }

  public Integer getCitationNumber() {
    return citationNumber;
  }

  public Integer getReglelogiqueNumber() {
    return reglelogiqueNumber;
  }

  public Integer getParagraphNumber() {
    return paragraphNumber;
  }
}
