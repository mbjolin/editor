package ca.mbjolin.editor.integration;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import ca.mbjolin.editor.config.AppConfig;
import ca.mbjolin.editor.facade.AntlrFacade;
import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.process._1_translation.TranslationProcessing;
import ca.mbjolin.editor.process._3_presentation.PresentationProcessing;
import ca.mbjolin.editor.util.CustomClassLoader;
import ca.mbjolin.editor.util.StringUtil;

public class TranslationToPresentationItgrTest {

  static private AntlrFacade antlrFacade;

  @BeforeAll
  public static void setup() {
    AppConfig config = new AppConfig();
    CustomClassLoader custom = new CustomClassLoader(config);
    antlrFacade = new AntlrFacade(config, custom);
  }

  @Test
  public void executeTraductionAndPresentationWithAsciidocTest() {
    // GIVEN
    TranslationProcessing translate = new TranslationProcessing(antlrFacade, "Asciidoc", new Journal());
    PresentationProcessing presentation =
        new PresentationProcessing(presentationAdocEx(), new Journal(), Boolean.FALSE);

    // WHEN
    translate.execute(asciidocEx1());
    presentation.execute(translate.getDocument());

    // THEN
    Journal tJou = translate.getJournal();
    Assertions.assertTrue(tJou.isWithoutError(), tJou.getMessagesAsString());

    Journal pJou = presentation.getJournal();
    Assertions.assertTrue(pJou.isWithoutError(), pJou.getMessagesAsString());

    // System.out.println(translate.getDocument().sections().toString());
    // System.out.println(presentation.getOut());
    Assertions.assertTrue(StringUtils.equals(asciidocEx1(), presentation.getOut()),
        StringUtil.compare(asciidocEx1(), presentation.getOut()));
  }
  
  @Test
  @Disabled
  public void executeTraductionAndPresentationWithAsciidocAndReStructTest() {
    // GIVEN
    TranslationProcessing translate = new TranslationProcessing(antlrFacade, "Asciidoc", new Journal());
    PresentationProcessing presentation =
        new PresentationProcessing(presentationAdocEx(), new Journal(), Boolean.FALSE);

    // WHEN
    translate.execute(asciidocEx1());
    presentation.execute(translate.getDocument());

    // THEN
    Journal tJou = translate.getJournal();
    Assertions.assertTrue(tJou.isWithoutError(), tJou.getMessagesAsString());

    Journal pJou = presentation.getJournal();
    Assertions.assertTrue(pJou.isWithoutError(), pJou.getMessagesAsString());

    // System.out.println(translate.getDocument().sections().toString());
    // System.out.println(presentation.getOut());
    Assertions.assertTrue(StringUtils.equals(asciidocEx1(), presentation.getOut()),
        StringUtil.compare(asciidocEx1(), presentation.getOut()));
  }


//@formatter:off

  public String asciidocEx1() {
    return """
:toc-title: "Table des matières"
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

  public String restructredTextEx1() {
    return """

""";
  }
//@formatter:on
}
