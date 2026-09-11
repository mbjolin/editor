package ca.mbjolin.editor.handler;

import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.event.Level;

import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.model.SimpleSession;
import ca.mbjolin.editor.process._3_presentation.PresentationProcessing;

public class PresentationHandler extends Handler {

  @Override
  public void setNext(Handler next) {
    this.next = next;
  }

  @Override
  public Boolean process(SimpleSession simpleSession) {
    Boolean isWithoutError = true;
    Journal journal = new Journal();

    journal.add(Level.INFO, "Début du traitement de présentation.");
    
    String transformationName = simpleSession.getSimpleConfig().get("mmdatteste").getTransformation();
    journal.add(Level.TRACE, "Nom de la transformation : " + transformationName);

    PresentationProcessing presentation =
        new PresentationProcessing(transformationName, journal, true);
    presentation.execute(simpleSession.getAtteste());
    journal.add(Level.TRACE, "Intrant (identifiant): " + simpleSession.getAtteste().identifiant());
    simpleSession.setPresente(presentation.getOut());
    journal.add(Level.TRACE, "Extrant (nbr de caractère): " + presentation.getOut().length());
    journal.add(Level.INFO, "Fin du traitement de présentation.");
    simpleSession.getSystemJ().concat(journal);
    TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.systemDefault()));
    if(presentation.getOut() != null) {
      simpleSession.setMoment_fin(new Date());
      if(simpleSession.getMoment_debut() == null) {
        simpleSession.setMoment_debut(new Date());
      }
    }

    if (isWithoutError && next != null) {
      next.process(simpleSession);
    }
    return isWithoutError;
  }


}
