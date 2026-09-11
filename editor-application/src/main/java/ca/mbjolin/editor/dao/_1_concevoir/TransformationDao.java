package ca.mbjolin.editor.dao._1_concevoir;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;

import ca.mbjolin.editor.dao.exception.IntegrityModelException;
import ca.mbjolin.gen.editor_ldm.concevoir_api.Routines;
import ca.mbjolin.gen.editor_ldm.concevoir_obj.udt.records.TransformationRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TransformationDao {

  private final Configuration config;

  @Inject
  public TransformationDao(Configuration config) {
    this.config = config;
  }

  public List<TransformationRecord> search(String query) {
    TransformationRecord[] result = Routines.searchTransformation(config, query);
    return Arrays.asList(result);
  }
  
  public List<TransformationRecord> getall() {
    TransformationRecord[] result = Routines.getallTransformation(config);
    return Arrays.asList(result);
  }

  public Optional<TransformationRecord> create(TransformationRecord Transformation) {
    return Optional.of(Routines.createTransformation(config, Transformation));
  }

  public Optional<TransformationRecord> read(TransformationRecord Transformation) {
    TransformationRecord result = Routines.readTransformation(config, Transformation);
    return Optional.of(result);
  }

  public TransformationRecord update(TransformationRecord newTransformation,
      TransformationRecord oldTransformation) throws IntegrityModelException {
    TransformationRecord result = null;
    try {
      result = Routines.updateTransformation(config, newTransformation, oldTransformation);
    } catch (Exception exception) {
      throw new IntegrityModelException(exception.getMessage());
    }
    return result;
  }

  public void delete(TransformationRecord Transformation) {
    Routines.deleteTransformation(config, Transformation);
  }
}
