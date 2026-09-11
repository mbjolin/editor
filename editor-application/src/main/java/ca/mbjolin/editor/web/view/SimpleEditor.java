package ca.mbjolin.editor.web.view;

import static java.util.Objects.requireNonNull;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/simpleeditor")
public class SimpleEditor {

  private final Template simpleeditor;

  public SimpleEditor(Template simpleeditor) {
    this.simpleeditor = requireNonNull(simpleeditor, "simpleeditor is required");
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance get() {

    TemplateInstance instance = simpleeditor.instance();

    return instance;
  }


}
