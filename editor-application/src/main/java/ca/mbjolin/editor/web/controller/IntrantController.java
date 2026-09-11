package ca.mbjolin.editor.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ca.mbjolin.editor.dao._3_atelier.IntrantDao;
import ca.mbjolin.editor.web.dto.ItemChoicejs;
import ca.mbjolin.gen.editor_ldm.atelier_obj.udt.records.IntrantRecord;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/intrant")
public class IntrantController {

  private final IntrantDao intrantDao;

  @Inject
  public IntrantController(IntrantDao intrantDao) {
    this.intrantDao = intrantDao;
  }

  @GET
  @Path("/search")
  public List<ItemChoicejs> findWithQuery(@QueryParam("e") String etape,
      @QueryParam("q") String query) {
    List<ItemChoicejs> result = new ArrayList<>();

    List<IntrantRecord> intrants = intrantDao.search(query);

    for (IntrantRecord intrant : intrants) {
      if (intrant.getEtape().getLiteral().equals(etape)) {
        result.add(new ItemChoicejs(intrant.getIdIntrant().toString(), intrant.getTitre()));
      }
    }

    return result;
  }

  /*
   * Il n'y a rien pour ordonnancer ces objets dans la bd de données relationnels.
   * Pour le moment, on les prends tous et on retourne une sous-liste.
   */
  @GET
  @Path("/last")
  public List<ItemChoicejs> getLastWithTypeAndNumber(@QueryParam("t") String etape,
      @QueryParam("n") Integer number) {
    List<ItemChoicejs> result = new ArrayList<>();

    List<IntrantRecord> intrants = intrantDao.getAll();

    for (IntrantRecord intrant : intrants) {
      if (intrant.getEtape().getLiteral().equals(etape)) {
        result.add(new ItemChoicejs(intrant.getIdIntrant().toString(), intrant.getTitre()));
      }
    }

    return result;
  }

  @GET
  @Path("/")
  public IntrantRecord get(@QueryParam("id") String id) {

    IntrantRecord input = new IntrantRecord();
    input.setIdIntrant(UUID.fromString(id));

    IntrantRecord result = intrantDao.read(input).get();

    return result;
  }

}
