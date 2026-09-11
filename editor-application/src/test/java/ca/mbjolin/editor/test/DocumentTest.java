package ca.mbjolin.editor.test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.deployment.util.FileUtil;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//@QuarkusTest
//@QuarkusTestResource(value = EditorLdmTestResource.class, restrictToAnnotatedClass = true)
public class DocumentTest {

//  ObjectMapper objectMapper = new ObjectMapper();
//
//  @Test
//  public void insertProductToDb() throws IOException {
//    // FileUtil.readFileContents(null)
//    // ClassPathUtils.
//    ClassLoader loader = Thread.currentThread().getContextClassLoader();
//    InputStream productDay1InputStream = loader.getResourceAsStream("input/product-27may.json");
//    byte[] productDay1Byte = FileUtil.readFileContents(productDay1InputStream);
//
//    // Product productDay1 = new Product("projectComponent", new ArrayList(), 22L, 11L,
//    // LocalDateTime.now(), new ArrayList());
//
//    List<Document> productList = objectMapper.readValue(productDay1Byte, List.class);
//
//    given().auth().preemptive().basic("admin", "lmmaam-admin").relaxedHTTPSValidation()
//        .contentType(ContentType.JSON)
//        .body(productList.get(0))
//        .when()
//        .post("/product")
//        .then()
//        .statusCode(204);
//  }
//
//  @Test
//  @Disabled
//  public void getProductFromDb() {
//    List<Document> products =
//        given().auth().preemptive().basic("admin", "lmmaam-admin").relaxedHTTPSValidation().when()
//            .get("/product")
//            .then()
//            .statusCode(200)
//            .extract()
//            .as(new TypeRef<>() {});
//    assertFalse(products.isEmpty());
//  }
}
