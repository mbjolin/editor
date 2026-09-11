package ca.mbjolin.editor.preprocess._1_config;

import org.slf4j.event.Level;

import ca.mbjolin.editor.journal.Journal;

public class ConfigPreprocessing {

  private Journal journal;

  public ConfigPreprocessing(Journal journal) {
    this.journal = journal;
  }

  public void execute(String content) {
    journal.add(Level.INFO, "Début du traitement de la configuration.");
    /* Éventuellement, cela servira à construire et vérifier le procédé plus dynamiquement. */
    journal.add(Level.INFO, "Fin du traitement de la configuration.");
  }

  public Journal getJournal() {
    return journal;
  }

}
