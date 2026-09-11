package ca.mbjolin.editor.process._2_intermediaterepresentation.regle;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;
import org.slf4j.event.Level;

import ca.mbjolin.editor.journal.Journal;

public class CustomRuleListener implements RuleListener{
  
  private Journal journal;

  public CustomRuleListener (Journal journal) {
    this.journal = journal;
  }

  /**
   * Triggered before the evaluation of a rule.
   *
   * @param rule being evaluated
   * @param facts known before evaluating the rule
   * @return true if the rule should be evaluated, false otherwise
   */
  public boolean beforeEvaluate(Rule rule, Facts facts) {
      return true;
  }

  /**
   * Triggered after the evaluation of a rule.
   *
   * @param rule that has been evaluated
   * @param facts known after evaluating the rule
   * @param evaluationResult true if the rule evaluated to true, false otherwise
   */
  public void afterEvaluate(Rule rule, Facts facts, boolean evaluationResult) { }

  /**
   * Triggered on condition evaluation error due to any runtime exception.
   *
   * @param rule that has been evaluated
   * @param facts known while evaluating the rule
   * @param exception that happened while attempting to evaluate the condition.
   */
  public void onEvaluationError(Rule rule, Facts facts, Exception exception) { 
    journal.add(Level.ERROR, exception.getMessage());
  }

  /**
   * Triggered before the execution of a rule.
   *
   * @param rule the current rule
   * @param facts known facts before executing the rule
   */
  public void beforeExecute(Rule rule, Facts facts) { }

  /**
   * Triggered after a rule has been executed successfully.
   *
   * @param rule the current rule
   * @param facts known facts after executing the rule
   */
  public void onSuccess(Rule rule, Facts facts) { }
  
  /**
   * Triggered after a rule has failed.
   *
   * @param rule the current rule
   * @param facts known facts after executing the rule
   * @param exception the exception thrown when attempting to execute the rule
   */
   public void onFailure(Rule rule, Facts facts, Exception exception) { 
     journal.add(Level.ERROR, exception.getMessage());
   }
  
}
