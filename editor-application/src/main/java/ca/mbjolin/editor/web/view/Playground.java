package ca.mbjolin.editor.web.view;

import static java.util.Objects.requireNonNull;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/playground")
public class Playground {

  private final Template playground;

  public Playground(Template playground) {
    this.playground = requireNonNull(playground, "playground is required");
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance get() {
    return playground.instance();
  }


}
