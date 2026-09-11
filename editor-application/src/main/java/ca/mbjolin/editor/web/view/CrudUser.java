package ca.mbjolin.editor.web.view;

import static java.util.Objects.requireNonNull;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/cruduser")
public class CrudUser {

  private final Template cruduser;

  public CrudUser(Template cruduser) {
    this.cruduser = requireNonNull(cruduser, "cruduser is required");
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance get() {
    TemplateInstance instance = cruduser.instance();
    return instance;
  }

}
