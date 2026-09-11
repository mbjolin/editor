package ca.mbjolin.editor.test;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import java.util.HashMap;
import java.util.Map;
import org.testcontainers.containers.GenericContainer;

public class EditorLdmTestResource implements QuarkusTestResourceLifecycleManager {

  GenericContainer<?> db;

  @Override
  public Map<String, String> start() {

    db = new GenericContainer<>("mbjolin.ca:5000/editor-ldm:latest")
        .withExposedPorts(5432);
        //.withImagePullPolicy(PullPolicy.alwaysPull());
    db.start();

    String urlDb = "postgresql://" + db.getHost() + ":" + db.getMappedPort(5432)
    + "/editor-ldm";

    Map<String, String> conf = new HashMap<>();

    conf.put("quarkus.datasource.reactive.url", urlDb);
    conf.put("quarkus.datasource.jdbc.url", "jdbc:" + urlDb);

    return conf;
  }

  @Override
  public void stop() {
    db.stop();
  }
}
