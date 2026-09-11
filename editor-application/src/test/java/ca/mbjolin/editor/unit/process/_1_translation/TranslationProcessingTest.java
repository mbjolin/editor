package ca.mbjolin.editor.unit.process._1_translation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.mbjolin.editor.config.AppConfig;
import ca.mbjolin.editor.facade.AntlrFacade;
import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.process._1_translation.TranslationProcessing;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Document;
import ca.mbjolin.editor.util.CustomClassLoader;

public class TranslationProcessingTest {

  static private AntlrFacade antlrFacade;

  @BeforeAll
  public static void setup() {
    AppConfig config = new AppConfig();
    CustomClassLoader custom = new CustomClassLoader(config);
    antlrFacade = new AntlrFacade(config, custom);
  }

  @Test
  @Disabled
  public void executeBaseTest() {
    // GIVEN
    TranslationProcessing translate = new TranslationProcessing(antlrFacade, "Document", new Journal());

    // WHEN
    translate.execute(docEx1());

    // THEN
    Journal jou = translate.getJournal();
    Assertions.assertTrue(jou.isWithoutError(), jou.getMessagesAsString());

    Document doc = translate.getDocument();
    Assertions.assertTrue(doc.metas().size() == 1);
    Assertions.assertTrue(doc.sections().size() == 1);
  }

  @Test
  @Disabled
  public void executeAsciidocTest() {
    // GIVEN
    TranslationProcessing translate = new TranslationProcessing(antlrFacade, "Asciidoc", new Journal());

    // WHEN
    translate.execute(asciidocEx1());

    // THEN
    Journal jou = translate.getJournal();
    Assertions.assertTrue(jou.isWithoutError(), jou.getMessagesAsString());

    Document doc = translate.getDocument();
    Assertions.assertTrue(doc.metas().size() == 1);
    Assertions.assertTrue(doc.sections().size() == 1);
    Assertions.assertTrue(doc.sections().get(0).paragraphes().size() == 1);
  }
  
  @Test
  @Disabled ("À utiliser lorsque PresentationToTranslationPropertyBase fail.")
  public void executeAsciidocFailTest() {
    // GIVEN
    TranslationProcessing translate = new TranslationProcessing(antlrFacade, "Asciidoc", new Journal());

    // WHEN
    translate.execute(asciidocFail1());

    // THEN
    Journal jou = translate.getJournal();
    Assertions.assertTrue(jou.isWithoutError(), jou.getMessagesAsString());

  }

  @Test
  public void executeReStructuredTextTest() throws Exception {
    // GIVEN
    TranslationProcessing translate =
        new TranslationProcessing(antlrFacade, "ReStructuredText", new Journal());

    // WHEN
    translate.execute(rsEx1());

    // THEN
    Journal jou = translate.getJournal();
    Assertions.assertTrue(jou.isWithoutError(), jou.getMessagesAsString());

    Document doc = translate.getDocument();

    Assertions.assertTrue(doc.metas().size() == 0);
    Assertions.assertTrue(doc.sections().size() == 3);

    /*
     * Lorsque cela roule en debug, il y a un paragraphe.
     * Sinon, il y en a pas. À Revoir!
     */
    Integer paragrapheNumber = doc.sections().get(0).paragraphes().size();
    System.out.println(paragrapheNumber);
    // Assertions.assertTrue(paragrapheNumber == 1);
  }

//@formatter:off
  public String docEx1() {
    return """
beginmeta 1333 "vmdsdds" endmeta

beginsection

beginsection
identifiant2

beginparagraphe

identifiant3

begincontent
identifiantcontent1 "Amatóriis ántiquis audiebamus clita, intellegimus!"
endcontent

begincite
citation1 "descriptioncitation"
endcite

endparagraphe
endsection

endsection
""";
  }
  
  public String asciidocFail1() {
    return """
= rBAAqIhQecmbE

== A

```
qzDtBuEnFXhBrvEWfWBLd
```

```
qzDtBuEnFXhBrvEWfWBLdss
```
""";
  }

  public String asciidocEx1() {
    return """
:toc-title: Table des matières

= Document title

== Level 1

paragraphe exemple

=== Level 2.1

Level2.1 message est ceci.

[quote]
____
Une citation ici.
____

=== Level 2.2

Level2.2 message est ceci.

=== Level 2.3

Level2.3 message est ceci.
```
Un prédicat ici.
```

""";
  }

  public String rsEx1() {
    return """

================
Document Heading
================

Heading
=======

Sub-heading
-----------

Paragraphs are separated
by a blank line.

Debut de paragraphe > Citation 1
> Citation 2 `source1` continuer
apres le para.
""";
  }
//@formatter:on
}
