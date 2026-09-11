package ca.mbjolin.editor.unit.process._3_presentation;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Contenu;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Document;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Meta;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Paragraphe;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Section;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Description;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Etiquette;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Identifiant;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Valeur;
import ca.mbjolin.editor.process._3_presentation.PresentationProcessing;

public class PresentationProcessingTest {


  @BeforeAll
  public static void setup() {}

  @Test
  public void executeBaseTest() {
    // GIVEN
    PresentationProcessing presentation =
        new PresentationProcessing(presentationAdocEx(), new Journal(), Boolean.FALSE);

    // WHEN
    presentation.execute(docEx1());

    // THEN
    Journal jou = presentation.getJournal();
    Assertions.assertTrue(jou.isWithoutError(), jou.getMessagesAsString());

    String doc = presentation.getOut();
    System.out.println(doc);
    Assertions.assertTrue(doc.contains("etiquette1"));
    Assertions.assertTrue(doc.contains("etiquette3"));
  }

  public Document docEx1() {
    Document doc = new Document(new Identifiant("id"), new Identifiant("projetId"),
        new ArrayList<>(), new ArrayList<>());
    doc.metas().add(
        new Meta(new Identifiant("meta1"), new Etiquette("etiquette1"), new Valeur("valeur1")));
    doc.metas().add(
        new Meta(new Identifiant("meta2"), new Etiquette("etiquette2"), new Valeur("valeur2")));

    List<Contenu> contenus = new ArrayList<>();
    contenus.add(new Contenu(new Identifiant("id"), new Description("description")));
    List<Paragraphe> paras = new ArrayList<>();
    paras.add(new Paragraphe(new Identifiant("para1"), contenus,
        new ArrayList<>(), new ArrayList<>()));
    doc.sections().add(
        new Section(new Identifiant("section1"), new Etiquette("etiquette3"), paras, null, true));
    return doc;
  }

//@formatter:off
  public String presentationEx1() {
    return 
"""
meta(identifiant, valeur) ::= <<
beginmeta <identifiant> "<valeur>" endmeta
>>

section(identifiant, paragraphes) ::= <<

beginsection
<identifiant>
<paragraphes>
endsection
>>
""";
  }
  
  public String presentationAdocEx() {
    return 
"""
meta(identifiant, etiquette, valeur) ::= <<
:<etiquette>: <valeur>
>>

doc(identifiant, projet, metas, sections) ::= <<
<metas>
<identifiant>
<sections>
>>

section(identifiant, etiquette, profondeur, paragraphes, sections) ::= <<
<etiquette><paragraphes><sections>
>>

paragraphe(contenus, reglelogiquex, citations) ::= <%
<contenus>
<reglelogiquex>
<citations>
%>

contenu(identifiant, description) ::= <<
<description>
>>

citation(identifiant, description, sources) ::= <<
[quote]
____
<description>____
\n
>>

reglelogiqueN(identifiant, descriptionNaturelle, sources) ::= <<
```
<descriptionNaturelle>```

>>

reglelogiqueF(identifiant, descriptionFormelle, sources) ::= <<
++++
<descriptionFormelle>++++

>>

source(identifiant, description) ::= <<
<identifiant>"<description>"
>>
""";
  }
//@formatter:on

}
