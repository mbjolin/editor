package ca.mbjolin.editor.web.view;

import static java.util.Objects.requireNonNull;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/dashboard/documenteditor")
public class DocumentEditor {

  private final Template documenteditor;


  public DocumentEditor(Template documenteditor) {
    this.documenteditor = requireNonNull(documenteditor, "documentdashboard is required");
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  public TemplateInstance get(@QueryParam("document") String documentId) {

    //"content", document.content()Document document = documentService.get(documentId);
//"content", document.content()
    TemplateInstance instance = documenteditor.instance();
    //instance.data("variableList", documentHisto.getProductsVar());

    return instance;
  }


}
