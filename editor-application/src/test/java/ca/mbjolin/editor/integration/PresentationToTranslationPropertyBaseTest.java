package ca.mbjolin.editor.integration;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;

import ca.mbjolin.editor.config.AppConfig;
import ca.mbjolin.editor.facade.AntlrFacade;
import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.process._1_translation.TranslationProcessing;
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
import ca.mbjolin.editor.process._3_presentation.PresentationProcessing;
import ca.mbjolin.editor.util.CustomClassLoader;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Disabled;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.PropertyDefaults;
import net.jqwik.api.Provide;
import net.jqwik.api.lifecycle.BeforeContainer;

@Disabled("Utile pour tester d'abord la grammaire.")
@PropertyDefaults(tries = 100)
public class PresentationToTranslationPropertyBaseTest {

  static private AntlrFacade antlrFacade;

  @BeforeContainer
  public static void setup() {
    AppConfig config = new AppConfig();
    CustomClassLoader custom = new CustomClassLoader(config);
    antlrFacade = new AntlrFacade(config, custom);
  }

  /* https://jqwik.net/docs/current/user-guide.html#string-not-blank */
  @Property
  public void generateDocumentAdocTest(@ForAll("documentRand") Document document)
      throws Exception {
    // GIVEN
    TranslationProcessing translate =
        new TranslationProcessing(antlrFacade, "Asciidoc", new Journal());
    PresentationProcessing presentation =
        new PresentationProcessing(presentationAdocEx(), new Journal(), Boolean.FALSE);

    // WHEN
    presentation.execute(document);
    System.out.println("=== BEGIN GENERATE DOC ===");
    System.out.println(presentation.getOut());
    System.out.println("=== END GENERATE DOC ===");
    translate.execute(presentation.getOut());

    // THEN
    Journal pJou = presentation.getJournal();
    Assertions.assertTrue(pJou.isWithoutError(), pJou.getMessagesAsString());

    Journal tJou = translate.getJournal();
    Assertions.assertTrue(tJou.isWithoutError(), tJou.getMessagesAsString());

  }

  @Provide
  Arbitrary<Document> documentRand() {

    Arbitrary<Identifiant> id = idRand();
    Arbitrary<Identifiant> idProject = idRand();
    Arbitrary<String> domain = Arbitraries.strings().alpha();
    Arbitrary<String> niveau = Arbitraries.strings().alpha();
    Arbitrary<List<Meta>> metas = metaRand();
    Arbitrary<List<Section>> sections = sectionRand(0);

    return Combinators.combine(id, idProject, metas, sections)
        .as((idV, idPV, metasV, sectionsV) -> new Document(idV, idPV, metasV, sectionsV));
  }


  @Provide
  Arbitrary<List<Meta>> metaRand() {
    Arbitrary<String> identifiants = Arbitraries.strings().alpha().ofMinLength(1);
    Arbitrary<String> etiquettes = Arbitraries.strings().alpha().ofMinLength(1);
    Arbitrary<String> valeurs = Arbitraries.strings().alpha().ofMinLength(1);

    return Combinators.combine(identifiants, etiquettes, valeurs)
        .as((identifiant, etiquette, valeur) -> new Meta(new Identifiant(identifiant),
            new Etiquette(etiquette), new Valeur(valeur)))
        .list().ofMinSize(0).ofMaxSize(4);
  }

  @Provide
  Arbitrary<List<Section>> sectionRand(int sectionNum) {

    Arbitrary<List<Section>> sections = null;
    Arbitrary<String> domain = Arbitraries.strings().alpha();
    Arbitrary<String> niveau = Arbitraries.strings().alpha();
    Arbitrary<Identifiant> identifiant = idRand();
    Arbitrary<Etiquette> etiquette = idEtiquette();
    Arbitrary<Description> description = descriptionRand();
    Arbitrary<List<Source>> sources = sourcesRand();

    Arbitrary<List<Contenu>> contenus = Combinators.combine(identifiant, description)
        .as((id, desc) -> new Contenu(id, desc)).list().ofMinSize(0)
        .ofMaxSize(2);

    Arbitrary<List<Citation>> citations = Combinators.combine(identifiant, description, sources)
        .as((id, desc, source) -> new Citation(id, desc, source)).list().ofMinSize(0)
        .ofMaxSize(1);

    Arbitrary<List<Reglelogique>> reglelogiques =
        Combinators.combine(identifiant, description, sources)
            .as((id, desc, source) -> new Reglelogique(id, desc, desc, source)).list().ofMinSize(0)
            .ofMaxSize(2);

    Arbitrary<List<Paragraphe>> paragraphes =
        Combinators.combine(identifiant, domain, niveau, contenus, citations, reglelogiques)
            .as((id, dom, niv, con, cit, pre) -> new Paragraphe(id, con, cit, pre)).list()
            .ofMinSize(0)
            .ofMaxSize(4);

    if (sectionNum > 0) {
      sectionNum = sectionNum - 1;
      sections = Combinators.combine(identifiant, etiquette, paragraphes, sectionRand(sectionNum))
          .as((id, eti, par, sec) -> new Section(id, eti, par, sec, true)).list().ofMinSize(0)
          .ofMaxSize(2);
    } else {
      sections = Combinators.combine(identifiant, etiquette, paragraphes)
          .as((id, eti, par) -> new Section(id, eti, par, new ArrayList<Section>(), true)).list()
          .ofMinSize(1)
          .ofMaxSize(1);
    }

    return sections;
  }

  @Provide
  Arbitrary<Identifiant> idRand() {
    Arbitrary<String> identifiant = Arbitraries.strings().alpha().ofMinLength(1);
    return Arbitraries.of(new Identifiant(identifiant.sample()));
  }

  @Provide
  Arbitrary<Etiquette> idEtiquette() {
    Arbitrary<String> etiquette = Arbitraries.strings().alpha().ofMinLength(1);
    return Arbitraries.of(new Etiquette(etiquette.sample()));
  }

  @Provide
  Arbitrary<Description> descriptionRand() {
    Arbitrary<String> description = Arbitraries.strings().alpha().ofMinLength(1);
    return Arbitraries.of(new Description(description.sample()));
  }

  @Provide
  Arbitrary<List<Source>> sourcesRand() {
    return Combinators.combine(idRand(), descriptionRand())
        .as((id, des) -> new Source(id, des)).list();
  }

  public String presentationAdocEx() {
    return """
        doc(identifiant, metas, sections) ::= <<
        <metas>
        = <identifiant>

        <sections>

        >>

        meta(identifiant, valeur) ::= <<
        :<identifiant>: <valeur>\n
        >>

        section(identifiant, paragraphes, sections, profondeur) ::= <<
        == <identifiant>\n
        <paragraphes>
        <sections>
        >>

        paragraphe(contenus, reglelogiques, citations) ::= <%
        <contenus>
        <reglelogiques>
        <citations>
        %>

        contenu(identifiant, description) ::= <<
        <description>
        >>

        reglelogique(identifiant, descriptionFormelle, descriptionNaturelle, sources) ::= <<
        ```
        <descriptionFormelle>
        ```


        >>

        citation(identifiant, description, sources) ::= <<
        [quote]
        ____
        <description>
        ____


        >>

        source(identifiant, description) ::= <<
        source:[<identifiant>] <description>
        >>

        """;
  }

}
