package ca.mbjolin.editor.model;

public class Command {

  private Etape etape;
  private String extension;
  private String transformation;

  public Command(Etape etape, String extension, String transformation) {
    this.etape = etape;
    this.extension = extension;
    this.transformation = transformation;
  }

  public Etape getEtape() {
    return etape;
  }

  public String getExtension() {
    return extension;
  }

  public String getTransformation() {
    return transformation;
  }
}


