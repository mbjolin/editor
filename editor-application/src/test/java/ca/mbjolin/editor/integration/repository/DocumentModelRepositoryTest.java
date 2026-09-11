package ca.mbjolin.editor.integration.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Document;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Paragraphe;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Section;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Etiquette;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Identifiant;
import ca.mbjolin.editor.repository.DocumentModelRepository;
import io.agroal.api.AgroalDataSource;
import io.agroal.api.configuration.supplier.AgroalPropertiesReader;

@Testcontainers
@Disabled("Nécessite d'avoir le jdd de base pour le moment.")
public class DocumentModelRepositoryTest {

  private static GenericContainer<?> database;

  private static Configuration config;

  @SuppressWarnings({"resource"})
  @BeforeAll
  public static void setup() throws SQLException {
    database =
        new GenericContainer<>("mbjolin.ca:5000/editor-ldm:latest/editor-ldm:latest")
            // .withImagePullPolicy(PullPolicy.alwaysPull())
            // .withEnv("DATASET", "base")
            .withExposedPorts(5432);
    database.start();

    String jdbcUrl = "jdbc:postgresql://" + database.getHost() + ":" + database.getMappedPort(5432)
        + "/editor-ldm";

    /* https://github.com/quarkusio/quarkus/issues/7019 */
    Map<String, String> props = new HashMap<>();

    props.put(AgroalPropertiesReader.MAX_SIZE, "10");
    props.put(AgroalPropertiesReader.MIN_SIZE, "10");
    props.put(AgroalPropertiesReader.INITIAL_SIZE, "10");
    props.put(AgroalPropertiesReader.MAX_LIFETIME_S, "300");
    props.put(AgroalPropertiesReader.ACQUISITION_TIMEOUT_S, "30");
    props.put(AgroalPropertiesReader.JDBC_URL, jdbcUrl);
    props.put(AgroalPropertiesReader.PRINCIPAL, "default-user");
    props.put(AgroalPropertiesReader.CREDENTIAL, "default-password");

    AgroalDataSource datasource = AgroalDataSource.from(new AgroalPropertiesReader()
        .readProperties(props)
        .get());

    config = new DefaultConfiguration()
        .set(datasource)
        .set(SQLDialect.POSTGRES);
  }

  @Test
  public void getDocumentModelFailTest() {
    // GIVEN
    DocumentModelRepository repo = new DocumentModelRepository(config, new Journal());
    Document document = new Document(new Identifiant("fakeid"), null, null, null);

    // WHEN
    Optional<Document> result = repo.getModele(document, true);
    Journal journal = repo.getJournal();

    // THEN
    Assertions.assertFalse(result.isPresent());
    Assertions.assertTrue(
        journal.getMessages().getLast().toString()
            .contains("Il n'y a pas exactement un document avec cet id"));
  }

  @Test
  public void getDocumentModelPassTest() {
    // GIVEN
    DocumentModelRepository repo = new DocumentModelRepository(config, new Journal());
    Document document = new Document(new Identifiant("iddocument1"), null, null, null);

    // WHEN
    Document result = repo.getModele(document, true).get();
    Journal journal = repo.getJournal();

    // THEN
    Assertions.assertEquals("iddocument1", result.identifiant().value());
    Assertions.assertEquals(0, journal.getMessages().size());
  }

  @Test
  public void createDocumentModelConvertFailTest() {
    // GIVEN
    DocumentModelRepository repo = new DocumentModelRepository(config, new Journal());
    Document document = new Document(new Identifiant("iddocument2"), null, null, null);

    // WHEN
    Optional<Document> result = repo.createModele(document);
    Journal journal = repo.getJournal();

    // THEN
    Assertions.assertFalse(result.isPresent());
    Assertions.assertTrue(
        journal.getMessages().getLast().toString().contains("Document.projet()\" is null"));
  }

  @Test
  public void createDocumentModelDbFailTest() {
    // GIVEN
    DocumentModelRepository repo = new DocumentModelRepository(config, new Journal());
    Document document = new Document(
        new Identifiant("iddocument2"), new Identifiant("idprojet2"),
        new ArrayList<>(), new ArrayList<>());

    // WHEN
    repo.verifyModele(document);
    Journal journal = repo.getJournal();

    // THEN
    Assertions.assertTrue(
        journal.getMessages().getLast().toString().contains(
            "La clé (id_projet)=(idprojet2) n'est pas présente dans la table « projet »"));
  }

  @Test
  public void verifyDocumentModelPassTest() {
    // GIVEN
    DocumentModelRepository repo = new DocumentModelRepository(config, new Journal());
    Document document = new Document(
        new Identifiant("iddocument2"), new Identifiant("idprojet1"),
        new ArrayList<>(), new ArrayList<>());

    // WHEN
    repo.verifyModele(document);
    Journal journal = repo.getJournal();

    // THEN
    Assertions.assertEquals(0, journal.getMessages().size());
  }

  @Test
  public void createDocumentModelPassTest() {
    // GIVEN
    DocumentModelRepository repo = new DocumentModelRepository(config, new Journal());
    Document document = new Document(
        new Identifiant("iddocument5"), new Identifiant("idprojet1"),
        new ArrayList<>(), new ArrayList<>());

    // WHEN
    Document result = repo.createModele(document).get();
    Journal journal = repo.getJournal();

    // THEN
    Assertions.assertEquals("iddocument5", result.identifiant().value());
    Assertions.assertEquals(0, journal.getMessages().size());
  }

  @Test
  public void createDocument2ModelPassTest() {
    // GIVEN
    DocumentModelRepository repo = new DocumentModelRepository(config, new Journal());
    Paragraphe para = new Paragraphe(new Identifiant("para1"),
        new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

    Section section =
        new Section(new Identifiant("section1"), new Etiquette("etiquette1"), new ArrayList<>(),
            new ArrayList<>(), true);
    section.paragraphes().add(para);

    Document document = new Document(
        new Identifiant("iddocument4"), new Identifiant("idprojet1"),
        new ArrayList<>(), new ArrayList<>());

    document.sections().add(section);

    // WHEN
    Document result = repo.createModele(document).get();
    Journal journal = repo.getJournal();

    // THEN
    Assertions.assertEquals("iddocument4", result.identifiant().value());
    Assertions.assertEquals(0, journal.getMessages().size());
  }

}
