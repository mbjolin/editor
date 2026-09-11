package ca.mbjolin.editor.service;

import java.util.UUID;

import org.jooq.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.mbjolin.editor.dao._1_concevoir.SimpleSessionDao;
import ca.mbjolin.editor.facade.AntlrFacade;
import ca.mbjolin.editor.handler.ConfigurationHandler;
import ca.mbjolin.editor.handler.EpurationHandler;
import ca.mbjolin.editor.handler.FormalisationHandler;
import ca.mbjolin.editor.handler.PresentationHandler;
import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.model.SimpleSession;
import ca.mbjolin.gen.editor_ldm.concevoir_obj.udt.records.SimplesessionRecord;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SimpleSessionService {

  private SimpleSessionDao simpleSessionDao;

  private AntlrFacade antlrFacade;

  private Configuration configDb;

  private ObjectMapper customObjectMapper;

  public SimpleSessionService(SimpleSessionDao simpleSessionDao, AntlrFacade antlrFacade,
      Configuration configDb, ObjectMapper customObjectMapper) {
    this.simpleSessionDao = simpleSessionDao;
    this.antlrFacade = antlrFacade;
    this.configDb = configDb;
    this.customObjectMapper = customObjectMapper;
  }

  public SimpleSession read(SimpleSession simpleSession) {
    SimpleSession newSession =
        new SimpleSession(simpleSessionDao.read(simpleSession.toSimpleSessionRecord()).get());
    return newSession;
  }

  public SimpleSession save(SimpleSession simpleSession) {
    SimpleSession newSession;
    if (simpleSession.getId_simplesession() == null) {
      simpleSession.setId_simplesession(UUID.randomUUID());
      newSession =
          new SimpleSession(simpleSessionDao.create(simpleSession.toSimpleSessionRecord()).get());
    } else {
      SimplesessionRecord oldSession =
          simpleSessionDao.read(simpleSession.toSimpleSessionRecord()).get();
      newSession = new SimpleSession(simpleSessionDao
          .update(simpleSession.toSimpleSessionRecord(), oldSession).get());
    }
    return newSession;
  }

  public SimpleSession execute(SimpleSession simpleSession) {
    Log.debug("Call execute");
    simpleSession.setSystemJ(new Journal());
    ConfigurationHandler config = new ConfigurationHandler(customObjectMapper);
    EpurationHandler epuration = new EpurationHandler(antlrFacade);
    FormalisationHandler formalisation = new FormalisationHandler(antlrFacade, configDb);
    PresentationHandler presentation = new PresentationHandler();

    config.setNext(epuration);
    epuration.setNext(formalisation);
    formalisation.setNext(presentation);

    config.process(simpleSession);

    return simpleSession;
  }
}
