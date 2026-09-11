package ca.mbjolin.editor.unit.preprocess.macro;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ca.mbjolin.editor.config.AppConfig;
import ca.mbjolin.editor.facade.AntlrFacade;
import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.preprocess._2_macro.MacroPreprocessing;
import ca.mbjolin.editor.util.CustomClassLoader;

public class MacroPreprocessingTest {

  static private AntlrFacade antlrFacade;

  @BeforeAll
  public static void setup() {
    AppConfig config = new AppConfig();
    CustomClassLoader custom = new CustomClassLoader(config);
    antlrFacade = new AntlrFacade(config, custom);
  }

  @Test
  public void executeBaseTest() {
    // Given
    MacroPreprocessing macroPreprocessing =
        new MacroPreprocessing(antlrFacade, "AsciidocMacro", new Journal());
    // When
    macroPreprocessing.execute(docEx5());
    // Then
    Journal jou = macroPreprocessing.getJournal();
    Assertions.assertTrue(jou.isWithoutError(), jou.getMessagesAsString());
  }

  @Test
  public void executeIncludeTest() {
    // Given
    MacroPreprocessing macroPreprocessing =
        new MacroPreprocessing(antlrFacade, "AsciidocMacro", new Journal());
    // When
    macroPreprocessing.addInclude(Map.of("include1.adoc", docEx3()));
    macroPreprocessing.execute(docEx2());
    // Then
    Journal jou = macroPreprocessing.getJournal();
    Assertions.assertTrue(jou.isWithoutError(), jou.getMessagesAsString());
  }

//@formatter:off
  public String docEx1() {

    return 
"""
:var1: vrai
ifdef::var1[]
a linterieur du condition {var1}
:var2: faux
endif::var1[]
{var2}
""";
  }

  public String docEx2() {
    return 
"""
include::include1.adoc[]
{variable1}
""";
  }

  public String docEx3() {
    return 
"""
:variable1: vrai

SuperContent
""";
  }
  
  public String docEx4() {

    return 
"""
ifdef::vartest[]
this content is {TESTINSIDE}
{TESTINSIDE2}
endif::vartest[]
{test0}
{{test00}}
{{test01}content{test02}}
{{test11}{test12}}
{test1{test2}test3}
{{test4}test5{test6}}
affiche{titre} 
:testvide: e
:testvar2: contenutestvar2
include::TESTMAX.adoc[]
include::ttaa3.test[]
aaaaaa
Contexte de support du document
include::test/est.ttt[]

:testvar: valuevar eewe 
ssasd
""";
  }
  
  public String docEx5() {

    return 
"""
:toc-title: Table des matières
:var1: vrai

= Document title

== Level 1

ifdef::var1[]
une utilisation de macro var1 = {var1}
endif::var1[]

paragraphe exemple

=== Level 2

Message contenant une réponse à une requête de tâche en cours d'un connecteur.
La réponse est au format JSON.
[quote]

____
Four score and seven years ago our fathers brought forth
on this continent a new nation...
____

""";
  }

//@formatter:on
}
