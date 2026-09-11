package ca.mbjolin.editor.dao._1_concevoir;

import java.util.Optional;

import org.jooq.Configuration;

import ca.mbjolin.gen.editor_ldm.concevoir_api.Routines;
import ca.mbjolin.gen.editor_ldm.concevoir_obj.udt.records.SessionRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SessionDao {

  private final Configuration config;

  @Inject
  public SessionDao(Configuration config) {
    this.config = config;
  }

  public Optional<SessionRecord> create(SessionRecord session) {
    return Optional.of(Routines.createSession(config, session));
  }

  public Optional<SessionRecord> read(SessionRecord session) {
    return Optional.of(Routines.readSession(config, session));
  }

  public Optional<SessionRecord> update(SessionRecord newSession, SessionRecord oldSession) {
    return Optional.of(Routines.updateSession(config, newSession, oldSession));
  }

  public void delete(SessionRecord session) {
    Routines.deleteSession(config, session);
  }
}
