package ca.mbjolin.editor.process._2_intermediaterepresentation;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.mvel.MVELRuleFactory;
import org.jeasy.rules.support.reader.YamlRuleDefinitionReader;
import org.mvel2.ParserContext;
import org.slf4j.event.Level;

import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Document;
import ca.mbjolin.editor.process._2_intermediaterepresentation.regle.CustomRuleListener;
import ca.mbjolin.editor.process._2_intermediaterepresentation.regle.RuleUtil;
import ca.mbjolin.editor.process._2_intermediaterepresentation.regle.WrapDoc;
import ca.mbjolin.editor.process._3_presentation.PresentationProcessing;

/* Comportement pour compléter le modèle intermédiaire. */
//Si un object du modèle n'existe pas dans la grammaire, on créer un par défaut.
//L'object META peuvent être rettaché par défault au document peu importe son emplacement.
//Un objet peut être transformé en autre objet selon des conditions.

/* Assertions sur le modèle intermédiaire. */
//Tous les symboles sont traduit dans le modèle.
//Les identifiants locaux sont unique au scope.
//Vérifier les citations et prédicats locaux existent dans le document.
//Section peut contenir des sections ou des paragraphes
//On peut exiger que tous les objets aient un identifiant.

/* Assertions sur le modèle base de données. */
//Vérifier les citations et prédicats globaux existent dans le système.
//Un identifiant global doit être unique au système.

/**
 * document.identifiant est vide
 * 
 * document.identifiant est vide donc document.idenfiant devient 'nouveauID'
 * 
 * document.identifiant est vide et document.description est vide donc
 * document.idenfiant devient genere aleatoire
 * 
 * 
 */


public class RuleProcessing {

  private Document documentOut;

  private Journal journal;

  public RuleProcessing(Journal journal) {
    this.journal = journal;
  }

  public String findRule(String name) {
    String content = "";
    InputStream is = PresentationProcessing.class.getClassLoader()
        .getResourceAsStream("antlr/rule/" + name + ".yml");
    if (is == null) {
      journal.add(Level.ERROR, "Il manque ce fichier de règles : ", name + ".yml");
    } else {
      try {
        content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
      } catch (IOException e) {
        journal.add(Level.ERROR, e.getMessage());
      } ;
    }
    return content;
  }

  public Document execute(Document document, String ruleFilename) {
    WrapDoc resultDoc = new WrapDoc();
    Facts facts = new Facts();
    facts.put("document", document);
    facts.put("resultdoc", resultDoc);

    String ruleString = findRule(ruleFilename);

    ParserContext context = new ParserContext();
    context.addImport("RuleUtil", RuleUtil.class);
    MVELRuleFactory ruleFactory = new MVELRuleFactory(new YamlRuleDefinitionReader(), context);
    Rules rules = null;
    try {
      rules = ruleFactory.createRules(new StringReader(ruleString));
      DefaultRulesEngine rulesEngine = new DefaultRulesEngine();
      rulesEngine.registerRuleListener(new CustomRuleListener(journal));

      rulesEngine.fire(rules, facts);

    } catch (Exception e) {
      journal.add(Level.ERROR, e.getMessage());
    }

    Document result = document;
    /* Advenant que les règles modifient le document. */
    if (resultDoc.getDoc() != null) {
      result = resultDoc.getDoc();
    }

    return result;
  }

  public Document getDocumentOut() {
    return documentOut;
  }

  public Journal getJournal() {
    return journal;
  }

}
