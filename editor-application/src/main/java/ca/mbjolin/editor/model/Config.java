package ca.mbjolin.editor.model;

import java.util.ArrayList;
import java.util.List;

public class Config {

  List<Command> commands = new ArrayList<>();

  String documentId;

  String projetId;

  String domaineParDefaut;

  String niveauParDefaut;

  String content;

  public Config(String content) {
    this.content = content;
  }

  public String toString() {
    return content;
  }

  private void toContent() {
    String configString = "projetId=" + projetId + "\n";
    configString = configString + "documentId=" + documentId + "\n";
    configString = configString + "domaineParDefaut=" + domaineParDefaut + "\n";
    configString = configString + "niveauParDefaut=" + niveauParDefaut + "\n";
    configString = configString + "Étape;Extension;Transformation\n";
    for (Command command : commands) {
      configString = configString + command.getEtape().getUiText() + ";" + command.getExtension()
          + ";" + command.getTransformation() + "\n";
    }
    content = configString;
  }

  public String toJson() {
    return toString();
  }

  public List<Command> getCommands() {
    return commands;
  }

  public void setCommands(List<Command> commands) {
    this.commands = commands;
  }

  public String getContent() {
    return content;
  }

  public String getDocumentId() {
    return documentId;
  }

  public void setDocumentId(String documentId) {
    this.documentId = documentId;
  }

  public String getProjetId() {
    return projetId;
  }

  public void setProjetId(String projetId) {
    this.projetId = projetId;
  }

  public String getDomaineParDefaut() {
    return domaineParDefaut;
  }

  public void setDomaineParDefaut(String domaineParDefaut) {
    this.domaineParDefaut = domaineParDefaut;
  }

  public String getNiveauParDefaut() {
    return niveauParDefaut;
  }

  public void setNiveauParDefaut(String niveauParDefaut) {
    this.niveauParDefaut = niveauParDefaut;
  }


}
