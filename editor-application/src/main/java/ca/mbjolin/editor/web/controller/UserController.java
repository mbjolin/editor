package ca.mbjolin.editor.web.controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;

import ca.mbjolin.editor.dao._1_concevoir.UserDao;
import ca.mbjolin.editor.dao.exception.IntegrityModelException;
import ca.mbjolin.editor.web.dto.Item;
import ca.mbjolin.editor.web.dto.SearchResult;
import ca.mbjolin.gen.editor_ldm.concevoir_obj.udt.records.UtilisateurRecord;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/user")
public class UserController {

  private final UserDao userDao;

  @Inject
  public UserController(UserDao userDao) {
    this.userDao = userDao;
  }

  @POST
  @Path("/create")
  public UtilisateurRecord create(UtilisateurRecord user) {
    return userDao.create(user).get();
  }

  @POST
  @Path("/read")
  public UtilisateurRecord read(UtilisateurRecord user) {
    return userDao.read(user).get();
  }

  @POST
  @Path("/update")
  /* Comment post 2 objects ?*/
  public UtilisateurRecord update(UtilisateurRecord user) throws IntegrityModelException {
    UtilisateurRecord current = null;
    current = userDao.update(user, user);
    return current;
  }

  @POST
  @Path("/delete")
  public void delete(UtilisateurRecord user) {
    userDao.delete(user);
  }


  @GET
  @Path("/search")
  public SearchResult findWithQuery(@QueryParam("q") String query)
      throws JsonProcessingException {
    SearchResult result = new SearchResult();

    List<UtilisateurRecord> resultUser = userDao.search(query);

    for(UtilisateurRecord current : resultUser) {
      //result.getItems().add(new Item(current.getUsername(),  current.formatJSON()));
      result.getItems().add(new Item(current.getUsername(),  current.intoMap(), "utilisateur"));
    }
    return result;
  }

  @GET
  @Path("/one")
  public UtilisateurRecord getOne() throws JsonProcessingException {

    List<UtilisateurRecord> resultUser = userDao.search("admin");
    UtilisateurRecord user = resultUser.get(0);

    return user;
  }

}
