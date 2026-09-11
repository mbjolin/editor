package ca.mbjolin.editor.dao._1_concevoir;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;

import ca.mbjolin.editor.dao.exception.IntegrityModelException;
import ca.mbjolin.gen.editor_ldm.concevoir_api.Routines;
import ca.mbjolin.gen.editor_ldm.concevoir_obj.udt.records.UtilisateurRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserDao {

  private final Configuration config;

  @Inject
  public UserDao(Configuration config) {
    this.config = config;
  }

  public List<UtilisateurRecord> search(String query) {
    UtilisateurRecord[] result = Routines.searchUtilisateur(config, query);
    return Arrays.asList(result);
  }

  public Optional<UtilisateurRecord> create(UtilisateurRecord utilisateur) {
    return Optional.of(Routines.createUtilisateur(config, utilisateur));
  }

  public Optional<UtilisateurRecord> read(UtilisateurRecord utilisateur) {
    UtilisateurRecord result = Routines.readUtilisateur(config, utilisateur);
    return Optional.of(result);
  }

  public UtilisateurRecord update(UtilisateurRecord newUtilisateur,
      UtilisateurRecord oldUtilisateur) throws IntegrityModelException {
    UtilisateurRecord result = null;
    try {
      result = Routines.updateUtilisateur(config, newUtilisateur, oldUtilisateur);
    } catch (Exception exception) {
      throw new IntegrityModelException(exception.getMessage());
    }
    return result;
  }

  public void delete(UtilisateurRecord utilisateur) {
    Routines.deleteUtilisateur(config, utilisateur);
  }
}
