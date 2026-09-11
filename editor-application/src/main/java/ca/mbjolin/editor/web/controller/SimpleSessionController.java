package ca.mbjolin.editor.web.controller;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.mbjolin.editor.dao._1_concevoir.SimpleSessionDao;
import ca.mbjolin.editor.model.SimpleSession;
import ca.mbjolin.editor.service.SimpleSessionService;
import ca.mbjolin.editor.web.dto.Item;
import ca.mbjolin.editor.web.dto.SearchResult;
import ca.mbjolin.gen.editor_ldm.concevoir_obj.udt.records.SimplesessionRecord;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/session")
public class SimpleSessionController {
  private final SimpleSessionDao simpleSessionDao;
  private final SimpleSessionService simpleSessionService;
  private final ObjectMapper customObjectMapper;

  @Inject
  public SimpleSessionController(SimpleSessionDao simpleSessionDao, SimpleSessionService simpleSessionService,
      ObjectMapper customObjectMapper) {
    this.simpleSessionDao = simpleSessionDao;
    this.simpleSessionService = simpleSessionService;
    this.customObjectMapper = new ObjectMapper();
  }

  @GET
  @Path("/search")
  public SearchResult findWithQuery(@QueryParam("q") String query) {
    SearchResult result = new SearchResult();

    List<SimplesessionRecord> sessions = simpleSessionDao.search(query);

    for (SimplesessionRecord session : sessions) {
      result.getItems().add(new Item(session.getIdSimplesession().toString(), session.intoMap(), "session"));
    }

    return result;
  }

  @GET
  @Path("/")
  public SimpleSession get(@QueryParam("id") String id) {
    SimpleSession newSimpleSession = new SimpleSession();
    newSimpleSession.setId_simplesession(UUID.fromString(id));
    return simpleSessionService.read(newSimpleSession);
  }

  @POST
  @Path("/saveString")
  public SimpleSession update(String session) {
    SimpleSession newSession = null;
    try {
      newSession = customObjectMapper.readValue(session, SimpleSession.class);
      newSession = simpleSessionService.save(newSession);
    } catch (JsonProcessingException e) {
      Log.error(e);
    }
    return newSession;
  }

  @POST
  @Path("/executeString")
  public SimpleSession execute(String session) {
    SimpleSession newSession = null;
    try {
      newSession = customObjectMapper.readValue(session, SimpleSession.class);
      newSession = simpleSessionService.execute(newSession);
    } catch (JsonProcessingException e) {
      Log.error(e);
    }
    return newSession;
  }

}
