package ca.mbjolin.editor.dao._1_concevoir;

import java.util.Optional;

import org.jooq.Configuration;

import ca.mbjolin.gen.editor_ldm.concevoir_api.Routines;
import ca.mbjolin.gen.editor_ldm.concevoir_obj.udt.records.ActionRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ActionDao {

  private final Configuration config;

  @Inject
  public ActionDao(Configuration config) {
    this.config = config;
  }

  public Optional<ActionRecord> create(ActionRecord action) {
    return Optional.of(Routines.createAction(config, action));
  }

  public Optional<ActionRecord> read(ActionRecord action) {
    return Optional.of(Routines.readAction(config, action));
  }

  public Optional<ActionRecord> update(ActionRecord newAction, ActionRecord oldAction) {
    return Optional.of(Routines.updateAction(config, newAction, oldAction));
  }

  public void delete(ActionRecord action) {
    Routines.deleteAction(config, action);
  }
}
