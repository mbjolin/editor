package ca.mbjolin.editor.handler;

import java.util.ArrayList;
import java.util.List;

import org.jooq.Configuration;
import org.slf4j.event.Level;

import ca.mbjolin.editor.facade.AntlrFacade;
import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.model.SimpleSession;
import ca.mbjolin.editor.process._1_translation.TranslationProcessing;
import ca.mbjolin.editor.process._2_intermediaterepresentation.IntermediateRepresentationStat;
import ca.mbjolin.editor.process._2_intermediaterepresentation.LogicSystemProcessing;
import ca.mbjolin.editor.process._2_intermediaterepresentation.RuleProcessing;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Document;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Meta;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Section;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Identifiant;
import ca.mbjolin.editor.repository.DocumentModelRepository;

public class FormalisationHandler extends Handler {

  private AntlrFacade antlrFacade;
  private Configuration configDb;

  public FormalisationHandler(AntlrFacade antlrFacade, Configuration configDb) {
    this.antlrFacade = antlrFacade;
    this.configDb = configDb;
  }

  @Override
  public void setNext(Handler next) {
    this.next = next;
  }

  @Override
  public Boolean process(SimpleSession simpleSession) {
    Boolean isWithoutError = true;
    Journal journal = new Journal();
    journal.add(Level.INFO, "Début du traitement de formalisation.");

    journal.add(Level.INFO, "Début de la traduction.");
    List<Document> documents = new ArrayList<Document>();

    if (simpleSession.getEpure1() != null && !simpleSession.getEpure1().isBlank()) {
      String transformationName = simpleSession.getSimpleConfig().get("epure1").getTransformation();
      journal.add(Level.TRACE, "Transformation (identifiant):" + transformationName);
      journal.add(Level.TRACE,
          "Intrant 1 (nbr de caractère):" + simpleSession.getEpure1().length());
      TranslationProcessing translateProcessing1 =
          new TranslationProcessing(antlrFacade, transformationName, journal);
      translateProcessing1.execute(simpleSession.getEpure1());
      documents.add(translateProcessing1.getDocument());
      translateProcessing1.getJournal();
      journal.add(Level.TRACE, "Extrant 1 (identifiant):"
          + translateProcessing1.getDocument().identifiant().value().strip());
    }

    if (simpleSession.getEpure2() != null && !simpleSession.getEpure2().isBlank()) {
      String transformationName = simpleSession.getSimpleConfig().get("epure2").getTransformation();
      journal.add(Level.TRACE, "Transformation (identifiant):" + transformationName);
      journal.add(Level.TRACE,
          "Intrant 2 (nbr de caractère):" + simpleSession.getEpure2().length());
      TranslationProcessing translateProcessing2 =
          new TranslationProcessing(antlrFacade, transformationName, journal);
      translateProcessing2.execute(simpleSession.getEpure2());
      translateProcessing2.getJournal();
      documents.add(translateProcessing2.getDocument());
      journal.add(Level.TRACE, "Extrant 2 (identifiant):"
          + translateProcessing2.getDocument().identifiant().value().strip());
    }
    journal.add(Level.INFO, "Fin de la traduction.");

    List<Meta> metas = new ArrayList<>();
    List<Section> sections = new ArrayList<>();
    for (Document document : documents) {
      if (document != null) {
        if (document.metas() != null) {
          metas.addAll(document.metas());
        }
        if (document.sections() != null) {
          sections.addAll(document.sections());
        }
      }
    }
    Document mergedDocument = new Document(new Identifiant(simpleSession.getTitre()),
        new Identifiant("projet" + simpleSession.getTitre()), metas, sections);

    simpleSession.setTraduit(mergedDocument);

    journal.add(Level.INFO, "Début de la vérification partielle et ajustement.");
    RuleProcessing ruleProcessing = new RuleProcessing(journal);
    Document afterAssuranceDocument = ruleProcessing.execute(mergedDocument,
        simpleSession.getSimpleConfig().get("mmd").getTransformation());
    if (afterAssuranceDocument == null) {
      journal.add(Level.ERROR, "La vérification partielle ne passe pas.");
      isWithoutError = false;
    }
    journal.add(Level.INFO, "Fin de la vérification partielle et ajustement.");
    simpleSession.setAssure(afterAssuranceDocument);

    journal.add(Level.INFO, "Début de la vérification totale.");


    DocumentModelRepository repo = new DocumentModelRepository(configDb, journal);
    Boolean isVerify = repo.verifyModele(afterAssuranceDocument);

    if (!isVerify) {
      journal.add(Level.ERROR, "Le SGBD a trouvé une erreur dans le modèle.");
      isWithoutError = false;
    }

    LogicSystemProcessing logicSystemProcessing = new LogicSystemProcessing(journal);
    Boolean resultLogic = logicSystemProcessing.execute(afterAssuranceDocument);
    
    if (!resultLogic) {
      journal.add(Level.ERROR, "Il y a une erreur de logique dans les règles du modèle.");
      isWithoutError = false;
    }

    simpleSession.setAtteste(afterAssuranceDocument);

    // ajouter le nombre de citation et pridicat et source etc.
    journal.add(Level.INFO, "Fin de la vérification totale.");

    IntermediateRepresentationStat stat = new IntermediateRepresentationStat(afterAssuranceDocument);
    journal.add(Level.TRACE, "Nombre de règle logique : " + stat.getReglelogiqueNumber());
    journal.add(Level.TRACE, "Nombre de citation : " + stat.getCitationNumber());

    simpleSession.getSystemJ().concat(journal);
    if (isWithoutError && next != null) {
      next.process(simpleSession);
    }
    return isWithoutError;
  }


}
