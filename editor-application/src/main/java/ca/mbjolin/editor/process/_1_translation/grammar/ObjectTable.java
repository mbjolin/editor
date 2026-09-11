package ca.mbjolin.editor.process._1_translation.grammar;

import java.util.HashMap;
import java.util.Map;


public class ObjectTable {

  private Map<Integer, Object> tables = new HashMap<>();

  public ObjectTable() {}

  public void add(Integer idObject, Object object) {
    tables.put(idObject, object);
  }

  public Map<Integer, Object> getTable() {
    return tables;
  }

  public Object resolve(Integer objectId) {
    return tables.get(objectId);
  }

}
