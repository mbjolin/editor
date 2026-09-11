package ca.mbjolin.editor.dao._3_atelier;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;

import ca.mbjolin.gen.editor_ldm.atelier_api.Routines;
import ca.mbjolin.gen.editor_ldm.atelier_obj.udt.records.AtelierRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AtelierDao {

  private final Configuration config;

  @Inject
  public AtelierDao(Configuration config) {
    this.config = config;
  }

  public List<AtelierRecord> search(String query) {
    AtelierRecord[] result = Routines.searchAtelier(config, query);
    return Arrays.asList(result);
  }

  public List<AtelierRecord> getAll() {
    AtelierRecord[] result = Routines.getallAtelier(config);
    return Arrays.asList(result);
  }

  public Optional<AtelierRecord> read(AtelierRecord atelier) {
    return Optional.of(Routines.readAtelier(config, atelier));
  }

  public Optional<AtelierRecord> create(AtelierRecord atelier) {
    return Optional.of(Routines.createAtelier(config, atelier));
  }

  public Optional<AtelierRecord> update(AtelierRecord atelier, AtelierRecord oldAtelier) {
    return Optional.of(Routines.updateAtelier(config, atelier, oldAtelier));
  }

  public void delete(AtelierRecord atelier) {
    Routines.deleteAtelier(config, atelier);
  }
}
