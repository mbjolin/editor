package ca.mbjolin.editor.integration.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Container.ExecResult;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import ca.mbjolin.editor.dao._1_concevoir.UserDao;
import ca.mbjolin.gen.editor_ldm.concevoir_obj.udt.records.UtilisateurRecord;
import ca.mbjolin.gen.editor_ldm.concevoir_pub.enums.Rôle;
import io.agroal.api.AgroalDataSource;
import io.agroal.api.configuration.supplier.AgroalPropertiesReader;

@Testcontainers
@Disabled("Nécessite d'avoir le jdd de base pour le moment.")
public class UserDaoTest {

  private GenericContainer<?> database;

  private Configuration config;

  @BeforeEach
  public void setup() throws SQLException, InterruptedException {
    database =
        new GenericContainer<>("mbjolin.ca:5000/editor-ldm:latest")
            // .withImagePullPolicy(PullPolicy.alwaysPull())
            .withEnv("DATASET", "base")
            .withEnv("PGPASSWORD", "default-password")
            .withExposedPorts(5432);
    database.start();
    TimeUnit.SECONDS.sleep(1);

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

  @AfterEach
  public void tearDown() {
    database.stop();
  }

  @Test
  public void getUserTest() {
    // GIVEN
    UserDao repo = new UserDao(config);
    UtilisateurRecord user = new UtilisateurRecord();
    user.setIdUtilisateur(UUID.fromString("00000000-0000-0000-0000-000000000001"));

    // WHEN
    Optional<UtilisateurRecord> result = repo.read(user);

    // THEN
    Assertions.assertTrue(result.isPresent());
  }

  @Test
  public void getUserAfterTableModificationTest()
      throws UnsupportedOperationException, IOException, InterruptedException {
    // GIVEN
    UserDao repo = new UserDao(config);
    UtilisateurRecord user = new UtilisateurRecord();
    user.setIdUtilisateur(UUID.fromString("00000000-0000-0000-0000-000000000001"));

    ExecResult modifyTable =
        database.execInContainer("psql", "-U", "default-user", "-d", "editor-ldm", "-c",
            "ALTER TABLE \"concevoir_pri\".\"utilisateur\" ADD \"nouvelle\" text;");

    String outModifyTable = modifyTable.getStdout().trim();

    // WHEN
    Optional<UtilisateurRecord> result = repo.read(user);

    // THEN
    Assertions.assertTrue(outModifyTable.contains("ALTER TABLE"));
    Assertions.assertTrue(result.isPresent());
  }

  @Test
  public void getUserAfterTableModificationColumnTest()
      throws UnsupportedOperationException, IOException, InterruptedException {
    // GIVEN
    UserDao repo = new UserDao(config);
    UtilisateurRecord user = new UtilisateurRecord();
    user.setIdUtilisateur(UUID.fromString("00000000-0000-0000-0000-000000000001"));

    ExecResult modifyTable1 =
        database.execInContainer("psql", "-U", "default-user", "-d", "editor-ldm", "-c",
            "ALTER TABLE \"concevoir_pri\".\"utilisateur\" ADD \"nouvelle2\" text NOT NULL default 'some';");

    ExecResult modifyTable2 =
        database.execInContainer("psql", "-U", "default-user", "-d", "editor-ldm", "-c",
            "ALTER TABLE \"concevoir_pri\".\"utilisateur\" RENAME COLUMN username TO bloublou;");

    ExecResult modifyTable3 =
        database.execInContainer("psql", "-U", "default-user", "-d", "editor-ldm", "-c",
            "ALTER TABLE \"concevoir_pri\".\"utilisateur\" ALTER COLUMN bloublou TYPE VARCHAR(255);");

    // ExecResult viewTable =
    // database.execInContainer("psql", "-U", "default-user", "-d", "editor-ldm", "-c",
    // "SELECT * FROM \"concevoir_pri\".\"utilisateur\";");

    String outModifyTable1 = modifyTable1.getStdout().trim();
    String outModifyTable2 = modifyTable2.getStdout().trim();
    String outModifyTable3 = modifyTable3.getStdout().trim();

    // WHEN
    UtilisateurRecord result = repo.read(user).get();

    // THEN
    Assertions.assertTrue(outModifyTable1.contains("ALTER TABLE"));
    Assertions.assertTrue(outModifyTable2.contains("ALTER TABLE"));
    Assertions.assertTrue(outModifyTable3.contains("ALTER TABLE"));
    Assertions.assertEquals(result.getUsername(), "admin");
  }

  @Test
  public void getUserAfterTableModificationWithConstraintTest()
      throws UnsupportedOperationException, IOException, InterruptedException {
    // GIVEN
    UserDao repo = new UserDao(config);
    UtilisateurRecord user = new UtilisateurRecord();
    user.setIdUtilisateur(UUID.fromString("00000000-0000-0000-0000-000000000005"));
    user.setRole(Rôle.user);
    user.setUsername("usernama");
    user.setPassword("pass");

    ExecResult modifyTable1 =
        database.execInContainer("psql", "-U", "default-user", "-d", "editor-ldm", "-c",
            "ALTER TABLE \"concevoir_pri\".\"utilisateur\" ADD \"nouvelle2\" text NOT NULL default 'some';");

    ExecResult modifyTable2 =
        database.execInContainer("psql", "-U", "default-user", "-d", "editor-ldm", "-c",
            "ALTER TABLE \"concevoir_pri\".\"utilisateur\" ALTER COLUMN \"nouvelle2\" DROP DEFAULT;");

    String outModifyTable1 = modifyTable1.getStdout().trim();
    String outModifyTable2 = modifyTable2.getStdout().trim();
    String error = "";
    // WHEN
    try {
      repo.create(user);
    } catch (Exception e) {
      error = e.getMessage();
    }

    // THEN
    Assertions.assertTrue(outModifyTable1.contains("ALTER TABLE"));
    Assertions.assertTrue(outModifyTable2.contains("ALTER TABLE"));
    Assertions.assertTrue(
        error.contains("une valeur NULL viole la contrainte NOT NULL de la colonne « nouvelle2 »"));
  }

  @Test
  public void getUserAfterTableModificationColumnDeleteTest()
      throws UnsupportedOperationException, IOException, InterruptedException {
    // GIVEN
    UserDao repo = new UserDao(config);
    UtilisateurRecord user = new UtilisateurRecord();
    user.setIdUtilisateur(UUID.fromString("00000000-0000-0000-0000-000000000007"));
    user.setRole(Rôle.user);
    user.setUsername("exemple");
    user.setPassword("pass");

    ExecResult modifyTable1 =
        database.execInContainer("psql", "-U", "default-user", "-d", "editor-ldm", "-c",
            "ALTER TABLE \"concevoir_pri\".\"utilisateur\" DROP COLUMN \"username\";");

    String outModifyTable1 = modifyTable1.getStdout().trim();

    // WHEN
    String error = "";
    Optional<UtilisateurRecord> result = Optional.empty();
    try {
      result = repo.create(user);
    } catch (Exception e) {
      error = e.getMessage();
    }

    // THEN
    Assertions.assertTrue(outModifyTable1.contains("ALTER TABLE"));
    Assertions.assertTrue(
        error.contains("la colonne « username » de la relation « utilisateur » n'existe pas"));
    Assertions.assertFalse(result.isPresent());
  }

  @Test
  public void getUserAfterTableModificationColumnOrderTest()
      throws UnsupportedOperationException, IOException, InterruptedException {
    // GIVEN
    UserDao repo = new UserDao(config);
    UtilisateurRecord user = new UtilisateurRecord();
    user.setIdUtilisateur(UUID.fromString("00000000-0000-0000-0000-000000000006"));
    user.setRole(Rôle.user);
    user.setUsername("exemple");
    user.setPassword("pass");

    ExecResult modifyTable1 =
        database.execInContainer("psql", "-U", "default-user", "-d", "editor-ldm", "-c",
            "ALTER TABLE \"concevoir_pri\".\"utilisateur\" DROP COLUMN \"username\";");

    ExecResult modifyTable2 =
        database.execInContainer("psql", "-U", "default-user", "-d", "editor-ldm", "-c",
            "ALTER TABLE \"concevoir_pri\".\"utilisateur\" ADD \"username2\" text NOT NULL default 'some1';");

    ExecResult modifyTable3 =
        database.execInContainer("psql", "-U", "default-user", "-d", "editor-ldm", "-c",
            "ALTER TABLE \"concevoir_pri\".\"utilisateur\" ADD \"username\" text NOT NULL default 'some2';");

    // ExecResult viewTable =
    // database.execInContainer("psql", "-U", "default-user", "-d", "editor-ldm", "-c",
    // "SELECT * FROM \"concevoir_pri\".\"utilisateur\";");

    String outModifyTable1 = modifyTable1.getStdout().trim();
    String outModifyTable2 = modifyTable2.getStdout().trim();
    String outModifyTable3 = modifyTable3.getStdout().trim();
    // WHEN
    String error = "";
    Optional<UtilisateurRecord> result = Optional.empty();
    try {
      result = repo.create(user);
    } catch (Exception e) {
      error = e.getMessage();
    }

    // THEN
    Assertions.assertTrue(outModifyTable1.contains("ALTER TABLE"));
    Assertions.assertTrue(outModifyTable2.contains("ALTER TABLE"));
    Assertions.assertTrue(outModifyTable3.contains("ALTER TABLE"));
    Assertions.assertTrue(error.isEmpty());
    Assertions.assertEquals("exemple", result.get().getUsername());
  }


}
