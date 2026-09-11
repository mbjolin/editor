package ca.mbjolin.editor.web.view;

import static java.util.Objects.requireNonNull;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/editor")
public class Editor {

  private final Template editor;

  public Editor(Template editor) {
    this.editor = requireNonNull(editor, "editor is required");
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance get() {

    TemplateInstance instance = editor.instance();

    return instance;
  }


}
