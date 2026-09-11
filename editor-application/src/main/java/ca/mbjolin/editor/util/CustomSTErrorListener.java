package ca.mbjolin.editor.util;

import org.slf4j.event.Level;
import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.misc.STMessage;

import ca.mbjolin.editor.journal.Journal;

/* https://stackoverflow.com/questions/27268522/how-to-retrieve-error-message-in-stringtemplate */
public class CustomSTErrorListener implements STErrorListener {

  private Journal journal;
  
  public CustomSTErrorListener ( Journal journal ) {
    this.journal = journal;
  }
  
  @Override
  public void compileTimeError(STMessage msg) {
    journal.add(Level.ERROR, "Compile time error.", msg.toString());
  }

  @Override
  public void runTimeError(STMessage msg) {
    journal.add(Level.ERROR, "runTimeError.", msg.toString());
  }

  @Override
  public void IOError(STMessage msg) {
    journal.add(Level.ERROR, "IOError.", msg.toString());
  }

  @Override
  public void internalError(STMessage msg) {
    journal.add(Level.ERROR, "internalError.", msg.toString());
  }

}
