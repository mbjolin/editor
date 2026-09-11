package ca.mbjolin.editor.process._2_intermediaterepresentation;

import java.util.ArrayList;
import java.util.List;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.slf4j.event.Level;

import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Document;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Paragraphe;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Reglelogique;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Section;
import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.ErrorWarning;
import edu.mit.csail.sdg.ast.Command;
import edu.mit.csail.sdg.parser.CompModule;
import edu.mit.csail.sdg.parser.CompUtil;
import edu.mit.csail.sdg.translator.A4Options;
import edu.mit.csail.sdg.translator.A4Solution;
import edu.mit.csail.sdg.translator.TranslateAlloyToKodkod;
import kodkod.engine.satlab.SATFactory;

/* Inspirée de :
 * https://alloytools.org/documentation/code/ExampleUsingTheCompiler.java.html 
 * https://alloytools.org/documentation/code/EvaluatorExample.java.html
 * */
public class LogicSystemProcessing {

  private Journal journal;

  Facts facts = new Facts();

  Rules rules = new Rules();

  A4Reporter rep = new A4Reporter() {
    @Override
    public void warning(ErrorWarning msg) {
      journal.add(Level.WARN, msg.toString().trim());
    }
  };

  public LogicSystemProcessing(Journal journal) {
    this.journal = journal;
  }

  public Boolean execute(Document document) {
    Boolean result = false;
    List<String> formalRule = new ArrayList<>();
    List<String> informalRule = new ArrayList<>();
    for (Section section : document.sections()) {
      for (Paragraphe para : section.paragraphes()) {
        for (Reglelogique rule : para.reglelogiquex()) {
          if (!rule.descriptionFormelle().isEmpty()) {
            if (rule.descriptionFormelle().value().contains("open")) /* Todo */
            {
              formalRule.addFirst(rule.descriptionFormelle().value());
            } else {
              formalRule.add(rule.descriptionFormelle().value());
            }
          } else if (!rule.descriptionNaturelle().isEmpty()) {
            informalRule.add(rule.descriptionNaturelle().value());
          }
        }
      }
    }

    String ruleString = String.join("", formalRule);
    try {
      CompModule model = CompUtil.parseEverything_fromString(rep, ruleString);
      A4Options opt = new A4Options();
      opt.solver = SATFactory.get("sat4j");
      Command cmd = model.getAllCommands().get(0);
      A4Solution sol =
          TranslateAlloyToKodkod.execute_command(rep, model.getAllReachableSigs(), cmd, opt);
      result = sol.satisfiable();
    } catch (Exception e) {
      journal.add(Level.ERROR, e.toString());
    }
    System.out.println(result);
    System.out.println(ruleString);
    return result;
  }


  public Journal getJournal() {
    return journal;
  }

}
