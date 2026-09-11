package ca.mbjolin.editor.handler;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.event.Level;

import ca.mbjolin.editor.facade.AntlrFacade;
import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.model.SimpleSession;
import ca.mbjolin.editor.preprocess._2_macro.MacroPreprocessing;

public class EpurationHandler extends Handler {

  private AntlrFacade antlrFacade;

  public EpurationHandler(AntlrFacade antlrFacade) {
    this.antlrFacade = antlrFacade;
  }

  @Override
  public void setNext(Handler next) {
    this.next = next;
  }

  @Override
  public Boolean process(SimpleSession simpleSession) {
    Boolean isWithoutError = true;
    Journal journal = new Journal();
    journal.add(Level.INFO, "Début du traitement de l'épuration.");
    journal.add(Level.TRACE, "Intrant 1 :" + simpleSession.getDescription1());
    journal.add(Level.TRACE, "Intrant 2 :" + simpleSession.getDescription2());

    if (simpleSession.getSimpleConfig().get("description1").getTransformation().equals("Identity")) {
      simpleSession.setEpure1(simpleSession.getDescription1());
      simpleSession.setEpure2(simpleSession.getDescription2());
    }
    /* Cas où deux adoc avec des macros includes. */
    else if(simpleSession.getSimpleConfig().get("description1").getTransformation()
        .equals(simpleSession.getSimpleConfig().get("description2").getTransformation())) {
      Map<String, String> includes = new HashMap<>();
      includes.put("description1", simpleSession.getDescription1());
      includes.put("description2", simpleSession.getDescription2());
      MacroPreprocessing macroPreprocessing =
          new MacroPreprocessing(antlrFacade, simpleSession.getSimpleConfig().get("description1").getTransformation(), journal);
      macroPreprocessing.addInclude(includes);
      macroPreprocessing.execute(simpleSession.getDescription1());
      simpleSession.setEpure1(macroPreprocessing.getOut());
      macroPreprocessing.getJournal();
    } else {
      
      MacroPreprocessing macroPreprocessing1 =
          new MacroPreprocessing(antlrFacade, simpleSession.getSimpleConfig().get("description1").getTransformation(), journal);
      macroPreprocessing1.execute(simpleSession.getDescription1());
      simpleSession.setEpure1(macroPreprocessing1.getOut());
      macroPreprocessing1.getJournal();
      
      MacroPreprocessing macroPreprocessing2 =
          new MacroPreprocessing(antlrFacade, simpleSession.getSimpleConfig().get("description2").getTransformation(), journal);
      macroPreprocessing2.execute(simpleSession.getDescription1());
      simpleSession.setEpure2(macroPreprocessing2.getOut());
      macroPreprocessing2.getJournal();
    }
  journal.add(Level.INFO,"Fin du traitement de l'épuration.");

  simpleSession.getSystemJ().concat(journal);
  if (isWithoutError && next != null) {
    next.process(simpleSession);
  }
  return isWithoutError;
  }
}
