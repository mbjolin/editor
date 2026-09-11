package ca.mbjolin.editor.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/*https://github.com/quarkusio/quarkus/issues/38511*/
/*https://stackoverflow.com/questions/70804601/in-quarkus-resteasy-how-do-i-show-helpful-error-messages-for-malformed-query-pa*/
@Provider
public class CustomExceptionMapper implements ExceptionMapper<Exception> {

  @Override
  public Response toResponse(Exception exception) {
    if (exception.getCause() instanceof JsonParseException ||
        exception.getCause() instanceof JsonMappingException) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("error parsing json body - " + exception.getMessage())
          .type(MediaType.APPLICATION_JSON)
          .build();
    }
    return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
        .entity(
            "{ \"error\" : \"" + exception.getMessage().replace("\"", "").replace("\n", "") + "\"}")
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
