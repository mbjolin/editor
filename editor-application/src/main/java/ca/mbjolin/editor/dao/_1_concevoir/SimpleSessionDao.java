package ca.mbjolin.editor.dao._1_concevoir;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;

import ca.mbjolin.gen.editor_ldm.concevoir_api.Routines;
import ca.mbjolin.gen.editor_ldm.concevoir_obj.udt.records.SimplesessionRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SimpleSessionDao {

  private final Configuration config;

  @Inject
  public SimpleSessionDao(Configuration config) {
    this.config = config;
  }
  
  public Optional<SimplesessionRecord> create(SimplesessionRecord session) {
    return Optional.of(Routines.createSimplesession(config, session));
  }

  public Optional<SimplesessionRecord> read(SimplesessionRecord session) {
    return Optional.of(Routines.readSimplesession(config, session));
  }

  public Optional<SimplesessionRecord> update(SimplesessionRecord newSession, SimplesessionRecord oldSession) {
    return Optional.of(Routines.updateSimplesession(config, newSession, oldSession));
  }

  public void delete(SimplesessionRecord session) {
    Routines.deleteSimplesession(config, session);
  }
  
  public List<SimplesessionRecord> search(String query) {
    SimplesessionRecord[] result = Routines.searchSimplesession(config, query);
    return Arrays.asList(result);
  }
}
