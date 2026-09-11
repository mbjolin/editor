package ca.mbjolin.editor.evaluation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.images.ImagePullPolicy;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.utility.MountableFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.mbjolin.editor.config.AppConfig;
import ca.mbjolin.editor.dao._1_concevoir.SimpleSessionDao;
import ca.mbjolin.editor.facade.AntlrFacade;
import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.model.SimpleSession;
import ca.mbjolin.editor.repository.DocumentModelRepository;
import ca.mbjolin.editor.service.DocumentModelService;
import ca.mbjolin.editor.service.SimpleSessionService;
import io.agroal.api.AgroalDataSource;
import io.agroal.api.configuration.supplier.AgroalPropertiesReader;

public class SimulateUserInterfaceInteraction {

  protected static SimpleSessionService sessionService;

  protected static DocumentModelService documentModelService;

  protected static ObjectMapper objectMapper;

  protected SimpleSession content;

  protected static GenericContainer<?> database;

  @BeforeEach
  public void setup() throws SQLException {
    database = new GenericContainer<>("mbjolin.ca:5000/editor-ldm:latest")
        .withCopyFileToContainer(MountableFile.forClasspathResource("init.sql"),
            "/docker-entrypoint-initdb.d/1-init.sql")
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

    Configuration config = new DefaultConfiguration()
        .set(datasource)
        .set(SQLDialect.POSTGRES);

    SimpleSessionDao sessionDao = new SimpleSessionDao(config);
    AntlrFacade antlrFacade = new AntlrFacade(new AppConfig(), null);
    objectMapper = new AppConfig().customObjectMapper();
    sessionService = new SimpleSessionService(sessionDao, antlrFacade, config, objectMapper);

    DocumentModelRepository documentModelRepository =
        new DocumentModelRepository(config, new Journal());
    documentModelService = new DocumentModelService(config);
  }

  protected SimpleSession loadDefaultContent(String path) throws IOException {
    InputStream is = getClass().getClassLoader()
        .getResourceAsStream(path);
    String defaultSession = new String(is.readAllBytes(), StandardCharsets.UTF_8);
    return objectMapper.readValue(defaultSession, SimpleSession.class);
  }

}
