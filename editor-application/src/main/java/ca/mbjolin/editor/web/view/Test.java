package ca.mbjolin.editor.web.view;

import static java.util.Objects.requireNonNull;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/test")
public class Test {

  private final Template test;

  public Test(Template test) {
    this.test = requireNonNull(test, "test is required");
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance get() {
    TemplateInstance instance = test.instance();
    return instance;
  }

}
