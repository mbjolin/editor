package ca.mbjolin.editor.config;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jooq.Configuration;
import org.jooq.SQLDialect;
import org.jooq.impl.DefaultConfiguration;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ca.mbjolin.editor.model.Config;
import ca.mbjolin.gen.editor_ldm.ace_obj.udt.records.TabRecord;
import ca.mbjolin.gen.editor_ldm.atelier_obj.udt.records.IntrantRecord;
import ca.mbjolin.gen.editor_ldm.concevoir_obj.udt.records.UtilisateurRecord;
import ca.mbjolin.gen.editor_ldm.document_obj.udt.records.DocumentRecord;
import io.agroal.api.AgroalDataSource;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class AppConfig {

  private final String javaDirectory;

  private final String classDirectory;

  public AppConfig() {
    File javaFile = new File("src/gen/java"); // peut être java, mais import ne fonctionne pas.
    javaFile.mkdir();
    javaDirectory = javaFile.getAbsolutePath() + "/";

    File classFile = new File("");// new File("class");
    classFile.mkdir();
    classDirectory = classFile.getAbsolutePath() + "/";
  }

  public String getJavaDirectory() {
    return javaDirectory;
  }

  public String getClassDirectory() {
    return classDirectory;
  }

  @Produces
  @ApplicationScoped
  public Configuration configuration(AgroalDataSource datasource) {
    // datasource.getConfiguration().
    Configuration config = new DefaultConfiguration()
        .set(datasource)
        .set(SQLDialect.POSTGRES);

    return config;
  }

  @Produces
  @ApplicationScoped
  public ObjectMapper customObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();

    module.addSerializer(UtilisateurRecord.class, new JsonSerializer<UtilisateurRecord>() {
      @Override
      public void serialize(UtilisateurRecord value, JsonGenerator gen,
          SerializerProvider serializers) throws IOException {
        Map<String, Object> data = value.intoMap();
        gen.writeObject(data);
      }
    });

    module.addDeserializer(UtilisateurRecord.class, new JsonDeserializer<UtilisateurRecord>() {
      public UtilisateurRecord deserialize(JsonParser p, DeserializationContext ctxt)
          throws IOException, JacksonException {
        TypeReference<HashMap<String, Object>> typeRef =
            new TypeReference<HashMap<String, Object>>() {};
        Map<String, Object> data = p.readValueAs(typeRef);
        UtilisateurRecord record = new UtilisateurRecord();
        record.fromMap(data);
        return record;
      };
    });

    module.addSerializer(DocumentRecord.class, new JsonSerializer<DocumentRecord>() {
      @Override
      public void serialize(DocumentRecord value, JsonGenerator gen,
          SerializerProvider serializers) throws IOException {
        Map<String, Object> data = value.intoMap();
        gen.writeObject(data);
      }
    });

    module.addDeserializer(DocumentRecord.class, new JsonDeserializer<DocumentRecord>() {
      public DocumentRecord deserialize(JsonParser p, DeserializationContext ctxt)
          throws IOException, JacksonException {
        TypeReference<HashMap<String, Object>> typeRef =
            new TypeReference<HashMap<String, Object>>() {};
        Map<String, Object> data = p.readValueAs(typeRef);
        DocumentRecord record = new DocumentRecord();
        record.fromMap(data);
        return record;
      };
    });

    module.addSerializer(IntrantRecord.class, new JsonSerializer<IntrantRecord>() {
      @Override
      public void serialize(IntrantRecord value, JsonGenerator gen,
          SerializerProvider serializers) throws IOException {
        Map<String, Object> data = value.intoMap();
        gen.writeObject(data);
      }
    });

    module.addDeserializer(IntrantRecord.class, new JsonDeserializer<IntrantRecord>() {
      public IntrantRecord deserialize(JsonParser p, DeserializationContext ctxt)
          throws IOException, JacksonException {
        TypeReference<HashMap<String, Object>> typeRef =
            new TypeReference<HashMap<String, Object>>() {};
        Map<String, Object> data = p.readValueAs(typeRef);
        IntrantRecord record = new IntrantRecord();
        record.fromMap(data);
        return record;
      };
    });

    module.addSerializer(TabRecord.class, new JsonSerializer<TabRecord>() {
      @Override
      public void serialize(TabRecord value, JsonGenerator gen,
          SerializerProvider serializers) throws IOException {
        Map<String, Object> data = value.intoMap();
        gen.writeObject(data);
      }
    });

    module.addDeserializer(TabRecord.class, new JsonDeserializer<TabRecord>() {
      public TabRecord deserialize(JsonParser p, DeserializationContext ctxt)
          throws IOException, JacksonException {
        TypeReference<HashMap<String, Object>> typeRef =
            new TypeReference<HashMap<String, Object>>() {};
        Map<String, Object> data = p.readValueAs(typeRef);
        TabRecord record = new TabRecord();
        record.fromMap(data);
        return record;
      };
    });

    module.addSerializer(Config.class, new ToStringSerializer());

    mapper.registerModule(module);
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    return mapper;
  }

}
