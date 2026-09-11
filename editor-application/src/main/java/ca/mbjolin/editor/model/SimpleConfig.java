package ca.mbjolin.editor.model;

public class SimpleConfig {

  String etape;
  
  String transformation;
  
  String intrant;
  
  String extrant;

  public SimpleConfig() {}

  public String getEtape() {
    return etape;
  }

  public void setEtape(String etape) {
    this.etape = etape;
  }

  public String getTransformation() {
    return transformation;
  }

  public void setTransformation(String transformation) {
    this.transformation = transformation;
  }

  public String getIntrant() {
    return intrant;
  }

  public void setIntrant(String intrant) {
    this.intrant = intrant;
  }

  public String getExtrant() {
    return extrant;
  }

  public void setExtrant(String extrant) {
    this.extrant = extrant;
  }
}
