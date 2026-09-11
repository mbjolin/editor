package ca.mbjolin.editor.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.event.Level;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.model.SimpleConfig;
import ca.mbjolin.editor.model.SimpleSession;

public class ConfigurationHandler extends Handler {

  private ObjectMapper customObjectMapper;

  public ConfigurationHandler() {}

  public ConfigurationHandler(ObjectMapper customObjectMapper) {
    this.customObjectMapper = customObjectMapper;
  }

  @Override
  public void setNext(Handler next) {
    this.next = next;
  }

  @Override
  public Boolean process(SimpleSession simpleSession) {
    Boolean isWithoutError = true;
    Journal journal = new Journal();
    journal.add(Level.INFO, "Début de la lecture de la configuration.");
    List<SimpleConfig> configList = new ArrayList<SimpleConfig>();
    try {
      configList = Arrays.asList(
          customObjectMapper.readValue(simpleSession.getTransformation(), SimpleConfig[].class));
    } catch (JsonProcessingException e) {
      journal.add(Level.ERROR, e.getOriginalMessage());
    }

    Map<String, SimpleConfig> configMap = new HashMap<String, SimpleConfig>();

    for (SimpleConfig config : configList) {
      configMap.put(config.getIntrant(), config);
    }
    simpleSession.setSimpleConfig(configMap);

    if (configMap.get("description1") == null) {
      journal.add(Level.ERROR, "Il doit y avoir une transformation sur description1.");
      isWithoutError = false;
    }

    if (configMap.get("description2") == null) {
      journal.add(Level.ERROR, "Il doit y avoir une transformation sur description2.");
      isWithoutError = false;
    }

    if (configMap.get("epure1") == null) {
      journal.add(Level.ERROR, "Il doit y avoir une transformation sur epure1.");
      isWithoutError = false;
    }

    if (configMap.get("epure2") == null) {
      journal.add(Level.ERROR, "Il doit y avoir une transformation sur epure2.");
      isWithoutError = false;
    }
    if (configMap.get("mmd") == null) {
      journal.add(Level.ERROR, "Il doit y avoir une transformation sur mmd.");
      isWithoutError = false;
    }
    
    if (configMap.get("mmdassure") == null) {
      journal.add(Level.ERROR, "Il doit y avoir une transformation sur mmdassure.");
      isWithoutError = false;
    }
    
    if (configMap.get("mmdatteste") == null) {
      journal.add(Level.ERROR, "Il doit y avoir une transformation sur mmdatteste.");
      isWithoutError = false;
    }

    if (simpleSession.getTitre() == null || simpleSession.getTitre().isBlank()) {
      journal.add(Level.ERROR, "La session doit avoir un titre.");
      isWithoutError = false;
    }
    journal.add(Level.TRACE, "Voici les transformations : " + simpleSession.getTransformation());
    journal.add(Level.INFO, "Fin de la lecture de la configuration.");
    
    
    if (simpleSession.getDescription1() == null || simpleSession.getDescription1().isBlank()) {
      journal.add(Level.DEBUG, "Il n'y a pas de description à traiter.");
      isWithoutError = false;
    }

    simpleSession.getSystemJ().concat(journal);
    if (isWithoutError && next != null) {
      next.process(simpleSession);
    }
    return isWithoutError;
  }

}
