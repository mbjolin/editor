package ca.mbjolin.editor.dao._3_atelier;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.Configuration;

import ca.mbjolin.gen.editor_ldm.atelier_api.Routines;
import ca.mbjolin.gen.editor_ldm.atelier_obj.udt.records.IntrantRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class IntrantDao {

  private final Configuration config;

  @Inject
  public IntrantDao(Configuration config) {
    this.config = config;
  }

  public List<IntrantRecord> search(String query) {
    IntrantRecord[] result = Routines.searchIntrant(config, query);
    return Arrays.asList(result);
  }

  public List<IntrantRecord> getAll() {
    IntrantRecord[] result = Routines.getallIntrant(config);
    return Arrays.asList(result);
  }

  public List<IntrantRecord> getWithIds(UUID[] intrantDb) {
    IntrantRecord[] result = Routines.getwithidsIntrant(config, intrantDb);
    return Arrays.asList(result);
  }

  public Optional<IntrantRecord> read(IntrantRecord intrant) {
    return Optional.of(Routines.readIntrant(config, intrant));
  }

  public Optional<IntrantRecord> create(IntrantRecord intrant) {
    return Optional.of(Routines.createIntrant(config, intrant));
  }

  public Optional<IntrantRecord> update(IntrantRecord intrant, IntrantRecord oldIntrant) {
    return Optional.of(Routines.updateIntrant(config, intrant, oldIntrant));
  }

}
