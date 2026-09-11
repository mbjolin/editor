package ca.mbjolin.editor.dao._4_ace;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jooq.Configuration;

import ca.mbjolin.gen.editor_ldm.ace_api.Routines;
import ca.mbjolin.gen.editor_ldm.ace_obj.udt.records.TabRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TabDao {

  private final Configuration config;

  @Inject
  public TabDao(Configuration config) {
    this.config = config;
  }

  public List<TabRecord> search(String query) {
    TabRecord[] result = Routines.searchTab(config, query);
    return Arrays.asList(result);
  }

  public List<TabRecord> getAll() {
    TabRecord[] result = Routines.getallTab(config);
    return Arrays.asList(result);
  }
  
  public List<TabRecord> getWithIds(UUID[] intrantUi) {
    TabRecord[] result = Routines.getwithidsTab(config, intrantUi);
    return Arrays.asList(result);
  }

  public Optional<TabRecord> read(TabRecord Tab) {
    return Optional.of(Routines.readTab(config, Tab));
  }

  public Optional<TabRecord> create(TabRecord tab) {
    return Optional.of(Routines.createTab(config, tab));
  }

  public Optional<TabRecord> update(TabRecord tab, TabRecord oldTab) {
    return Optional.of(Routines.updateTab(config, tab, oldTab));
  }
}
