package ca.mbjolin.editor.model;

import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Document;
import ca.mbjolin.gen.editor_ldm.concevoir_obj.udt.records.SimplesessionRecord;

/* Session simple pour réaliser des tests pour la preuve de concept. */
public class SimpleSession {

  private UUID id_simplesession;

  private String titre;

  private Journal systemJ = new Journal();

  private String description1;

  private String description2;

  /* temporaire */
  private String epure1;

  private String epure2;
  
  private Document traduit;

  private Document assure;

  private Document atteste;
  
  private String presente;

  private Map<String, SimpleConfig> simpleConfig;
  
  @JsonInclude(Include.NON_NULL)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone="America/Toronto")
  private Date moment_debut;

  @JsonInclude(Include.NON_NULL)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone="America/Toronto")
  private Date moment_fin;

  private String transformation;
  public SimpleSession() {}
  public SimpleSession(SimplesessionRecord record) {
    this.id_simplesession = record.getIdSimplesession();
    this.titre = record.getTitre();
    this.systemJ = new Journal(record.getSystemj());
    this.description1 = record.getDescription1();
    this.description2 = record.getDescription2();
    this.presente = record.getPresente();
    this.moment_debut = java.sql.Timestamp.valueOf(record.getMomentDebut());
    this.moment_fin = java.sql.Timestamp.valueOf(record.getMomentFin());
    this.transformation = record.getTransformation();
  }


  public Document getAssure() {
    return assure;
  }
  public Document getAtteste() {
    return atteste;
  }
  public String getDescription1() {
    return description1;
  }
  public String getDescription2() {
    return description2;
  }
  public String getEpure1() {
    return epure1;
  }
  public String getEpure2() {
    return epure2;
  }
  public UUID getId_simplesession() {
    return id_simplesession;
  }
  public Date getMoment_debut() {
    return moment_debut;
  }
  public Date getMoment_fin() {
    return moment_fin;
  }
  public String getPresente() {
    return presente;
  }
  public Map<String, SimpleConfig> getSimpleConfig() {
    return simpleConfig;
  }
  public Journal getSystemJ() {
    return systemJ;
  }
  public String getTitre() {
    return titre;
  }
  public Document getTraduit() {
    return traduit;
  }
  public String getTransformation() {
    return transformation;
  }
  public void setAssure(Document assure) {
    this.assure = assure;
  }
  public void setAtteste(Document atteste) {
    this.atteste = atteste;
  }
  public void setDescription1(String description1) {
    this.description1 = description1;
  }
  public void setDescription2(String description2) {
    this.description2 = description2;
  }
  public void setEpure1(String epure1) {
    this.epure1 = epure1;
  }

  public void setEpure2(String epure2) {
    this.epure2 = epure2;
  }
  public void setId_simplesession(UUID id_simplesession) {
    this.id_simplesession = id_simplesession;
  }
  public void setMoment_debut(Date moment_debut) {
    this.moment_debut = moment_debut;
  }
  public void setMoment_fin(Date moment_fin) {
    this.moment_fin = moment_fin;
  }
  public void setPresente(String presente) {
    this.presente = presente;
  }
  public void setSimpleConfig(Map<String, SimpleConfig> simpleConfig) {
    this.simpleConfig = simpleConfig;
  }
  public void setSystemJ(Journal systemJ) {
    this.systemJ = systemJ;
  }
  public void setTitre(String titre) {
    this.titre = titre;
  }
  public void setTraduit(Document traduit) {
    this.traduit = traduit;
  }
  public void setTransformation(String transformation) {
    this.transformation = transformation;
  }
  public SimplesessionRecord toSimpleSessionRecord() {
    SimplesessionRecord record = new SimplesessionRecord();
    record.setIdSimplesession(id_simplesession);
    record.setTitre(titre);
    record.setSystemj(systemJ.getMessagesAsString());

    record.setDescription1(description1);
    record.setDescription2(description2);
    record.setPresente(presente);
    record.setMomentDebut(moment_debut.toInstant().atZone(ZoneId.systemDefault())
        .toLocalDateTime());
    record.setMomentFin(moment_fin.toInstant().atZone(ZoneId.systemDefault())
        .toLocalDateTime());
    record.setTransformation(transformation);
    return record;
  }

}
