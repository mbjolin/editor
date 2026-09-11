package ca.mbjolin.editor.process._1_translation;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.event.Level;

import ca.mbjolin.editor.facade.AntlrFacade;
import ca.mbjolin.editor.journal.Journal;
import ca.mbjolin.editor.process._1_translation.grammar.ObjectTable;
import ca.mbjolin.editor.process._1_translation.grammar.Symbol;
import ca.mbjolin.editor.process._1_translation.grammar.SymbolAttribut;
import ca.mbjolin.editor.process._1_translation.grammar.SymbolTable;
import ca.mbjolin.editor.process._1_translation.grammar.SymbolType;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Citation;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Contenu;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Document;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Meta;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Paragraphe;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Reglelogique;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Section;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.Source;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Description;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Etiquette;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Identifiant;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Valeur;
import ca.mbjolin.editor.util.LoggingErrorListener;

public class TranslationProcessing {

  private Document document;

  private SymbolTable symbolTable;

  private ObjectTable objectTable;

  private AntlrFacade antlrFacade;

  private Journal journal;

  private String grammarName;

  private List<String> lexicalGrammarErrors = new ArrayList<String>();

  private List<String> syntaxGrammarErrors = new ArrayList<String>();

  private List<String> symbolErrors = new ArrayList<String>();

  public TranslationProcessing(AntlrFacade antlrFacade, String transformation, Journal journal) {
    this.antlrFacade = antlrFacade;
    this.grammarName = transformation;
    this.journal = journal;
  }

  public void convert() {
    objectTable = new ObjectTable();
    List<Integer> documents = new ArrayList<Integer>();
    List<Integer> metas = new ArrayList<Integer>();
    List<Integer> sections = new ArrayList<Integer>();
    List<Integer> paragraphes = new ArrayList<Integer>();
    List<Integer> contenus = new ArrayList<Integer>();
    List<Integer> citations = new ArrayList<Integer>();
    List<Integer> reglelogiques = new ArrayList<Integer>();
    List<Integer> sources = new ArrayList<Integer>();
    for (Symbol symbol : symbolTable.getTable().values()) {
      switch (symbol.getType()) {
        case DOCUMENT -> {
          Identifiant id = new Identifiant(symbol.getAttribut(SymbolAttribut.IDENTIFIANT));
          Identifiant projet = new Identifiant(symbol.getAttribut(SymbolAttribut.PROJET));
          Document doc = new Document(id, projet, new ArrayList<>(), new ArrayList<>());
          documents.add(symbol.getIdObject());
          objectTable.add(symbol.getIdObject(), doc);
        }
        case META -> {
          Identifiant id = new Identifiant(symbol.getAttribut(SymbolAttribut.IDENTIFIANT));
          Etiquette etiquette = new Etiquette(symbol.getAttribut(SymbolAttribut.ETIQUETTE));
          Valeur valeur = new Valeur(symbol.getAttribut(SymbolAttribut.VALEUR));
          Meta meta = new Meta(id, etiquette, valeur);
          metas.add(symbol.getIdObject());
          objectTable.add(symbol.getIdObject(), meta);
        }
        case SECTION -> {
          Identifiant id = new Identifiant(symbol.getAttribut(SymbolAttribut.IDENTIFIANT));
          Etiquette etiquette = new Etiquette(symbol.getAttribut(SymbolAttribut.ETIQUETTE));
          Section section = new Section(id, etiquette, new ArrayList<>(), new ArrayList<>(), false);
          sections.add(symbol.getIdObject());
          objectTable.add(symbol.getIdObject(), section);
        }
        case PARAGRAPHE -> {
          Identifiant id = new Identifiant(symbol.getAttribut(SymbolAttribut.IDENTIFIANT));
          Paragraphe para = new Paragraphe(id, new ArrayList<>(),
              new ArrayList<>(), new ArrayList<>());
          paragraphes.add(symbol.getIdObject());
          objectTable.add(symbol.getIdObject(), para);
        }
        case CONTENU -> {
          Identifiant id = new Identifiant(symbol.getAttribut(SymbolAttribut.IDENTIFIANT));
          Description description = new Description(symbol.getAttribut(SymbolAttribut.DESCRIPTION));
          Contenu contenu = new Contenu(id, description);
          contenus.add(symbol.getIdObject());
          objectTable.add(symbol.getIdObject(), contenu);
        }
        case CITATION -> {
          Identifiant id = new Identifiant(symbol.getAttribut(SymbolAttribut.IDENTIFIANT));
          Description description = new Description(symbol.getAttribut(SymbolAttribut.DESCRIPTION));
          Citation citation = new Citation(id, description, new ArrayList<>());
          citations.add(symbol.getIdObject());
          objectTable.add(symbol.getIdObject(), citation);
        }
        case REGLELOGIQUE -> {
          Identifiant id = new Identifiant(symbol.getAttribut(SymbolAttribut.IDENTIFIANT));
          Description description = new Description(symbol.getAttribut(SymbolAttribut.DESCRIPTION));
          Description descriptionF =
              new Description(symbol.getAttribut(SymbolAttribut.DESCRIPTIONF));
          Reglelogique reglelogique = new Reglelogique(id, descriptionF, description, new ArrayList<>());
          reglelogiques.add(symbol.getIdObject());
          objectTable.add(symbol.getIdObject(), reglelogique);
        }
        case SOURCE -> {
          Identifiant id = new Identifiant(symbol.getAttribut(SymbolAttribut.IDENTIFIANT));
          Description desc = new Description(symbol.getAttribut(SymbolAttribut.DESCRIPTION));
          Source source = new Source(id, desc);
          sources.add(symbol.getIdObject());
          objectTable.add(symbol.getIdObject(), source);
        }
        default -> throw new IllegalArgumentException("Unexpected value: " + symbol.getType());
      }
    }
    for (Integer sourceObjectId : sources) {
      Symbol symbol = symbolTable.resolve(sourceObjectId);
      for (Integer parent : symbol.getParents()) {
        Symbol parentSymbol = symbolTable.resolve(parent);
        if (parentSymbol != null) {
          if (parentSymbol.getType() == SymbolType.CITATION) {
            Citation citation = (Citation) objectTable.resolve(parentSymbol.getIdObject());
            Source source = (Source) objectTable.resolve(sourceObjectId);
            citation.sources().add(source);
            break;
          } else if (parentSymbol.getType() == SymbolType.REGLELOGIQUE) {
            Reglelogique reglelogique = (Reglelogique) objectTable.resolve(parentSymbol.getIdObject());
            Source source = (Source) objectTable.resolve(sourceObjectId);
            reglelogique.sources().add(source);
            break;
          }
        }
      }
    }

    for (Integer contenuObjectId : contenus) {
      Symbol symbol = symbolTable.resolve(contenuObjectId);
//      System.out.println(symbol.getType() + " " + symbol.getAttribut(SymbolAttribut.DESCRIPTION)
//          + " " + symbol.getParents());
      for (Integer parent : symbol.getParents()) {
        Symbol parentSymbol = symbolTable.resolve(parent);
        if (parentSymbol != null) {
//          System.out.println(parentSymbol.getIdObject() + " : " + parentSymbol.getType());
          if (parentSymbol.getType() == SymbolType.PARAGRAPHE) {
            Paragraphe paragraphe = (Paragraphe) objectTable.resolve(parentSymbol.getIdObject());
            Contenu contenu = (Contenu) objectTable.resolve(contenuObjectId);
            paragraphe.contenus().add(contenu);
            break;
          }
        }
      }
    }

    for (Integer objectId : citations) {
      Symbol symbol = symbolTable.resolve(objectId);
      for (Integer parent : symbol.getParents()) {
        Symbol parentSymbol = symbolTable.resolve(parent);
        if (parentSymbol != null) {
          if (parentSymbol.getType() == SymbolType.PARAGRAPHE) {
            Paragraphe paragraphe = (Paragraphe) objectTable.resolve(parentSymbol.getIdObject());
            Citation citation = (Citation) objectTable.resolve(objectId);
            paragraphe.citations().add(citation);
            break;
          }
        }
      }
    }

    for (Integer objectId : reglelogiques) {
      Symbol symbol = symbolTable.resolve(objectId);
      for (Integer parent : symbol.getParents()) {
        Symbol parentSymbol = symbolTable.resolve(parent);
        if (parentSymbol != null) {
          if (parentSymbol.getType() == SymbolType.PARAGRAPHE) {
            Paragraphe paragraphe = (Paragraphe) objectTable.resolve(parentSymbol.getIdObject());
            Reglelogique reglelogique = (Reglelogique) objectTable.resolve(objectId);
            paragraphe.reglelogiquex().add(reglelogique);
            break;
          }
        }
      }
    }

    for (Integer objectId : paragraphes) {
      Symbol symbol = symbolTable.resolve(objectId);
//      System.out.println(symbol.getType() + " " + symbol.getAttribut(SymbolAttribut.DESCRIPTION)
//          + " " + symbol.getParents());
      for (Integer parent : symbol.getParents()) {
        Symbol parentSymbol = symbolTable.resolve(parent);
        if (parentSymbol != null) {
//          System.out.println(parentSymbol.getIdObject() + " : " + parentSymbol.getType());
          if (parentSymbol.getType() == SymbolType.SECTION) {
            Section section = (Section) objectTable.resolve(parentSymbol.getIdObject());
            Paragraphe paragraphe = (Paragraphe) objectTable.resolve(objectId);
            section.paragraphes().add(paragraphe);
            break;
          }
        }
      }
    }
//    for (Integer objectId : sections) {
//      Symbol symbol = symbolTable.resolve(objectId);
//      System.out.println(symbol.getAttribut(SymbolAttribut.IDENTIFIANT));
//      System.out.println("PARENT:" + symbol.getParents().size());
//      for (Integer parent : symbol.getParents()) {
//        Symbol parentSymbol = symbolTable.resolve(parent);
//        System.out.println(parent+":" +parentSymbol.getAttribut(SymbolAttribut.IDENTIFIANT));
//      }
//      System.out.println("------------");
//    }

    document = (Document) objectTable.resolve(documents.get(0));
    
    for (Integer objectId : sections) {
      Symbol symbol = symbolTable.resolve(objectId);
      for (Integer parent : symbol.getParents()) {
        Symbol parentSymbol = symbolTable.resolve(parent);
        if (parentSymbol != null) {
          if (parentSymbol.getType() == SymbolType.SECTION) {
            Section parentSection = (Section) objectTable.resolve(parentSymbol.getIdObject());
            Section section = (Section) objectTable.resolve(objectId);
            parentSection.sections().add(section);
            document.sections().add(section);
            break;
          } else if (parentSymbol.getType() == SymbolType.DOCUMENT) {
            Section section = (Section) objectTable.resolve(objectId);
            document.sections().add(section.withRacinaire(true));
            break;
          }
        }
      }
    }

    for (Integer objectId : metas) {
      Symbol symbol = symbolTable.resolve(objectId);
      for (Integer parent : symbol.getParents()) {
        Symbol parentSymbol = symbolTable.resolve(parent);
        if (parentSymbol != null) {
          if (parentSymbol.getType() == SymbolType.DOCUMENT) {
            Document document = (Document) objectTable.resolve(parentSymbol.getIdObject());
            Meta meta = (Meta) objectTable.resolve(objectId);
            document.metas().add(meta);
            break;
          }
        }
      }
    }

  }

  public void symbolVerification() throws Exception {
    // tables.remove(tables.keySet().toArray()[3]); Un test.
    // for (Symbol symbol : symbolTable.getTable().values()) {
    // for (SymbolType type : symbol.getComposites().keySet()) {
    // for (Integer id : symbol.getComposites(type)) {
    // if (!symbolTable.getTable().containsKey(id)) {
    // symbolErrors.add("Il manque ce symbole : " + symbol.getType() + "." + type + "." + id);
    // }
    // }
    // }
    // }
  }

  public void execute(String content) {
    compileContentWithGrammar(content, grammarName);
  }

  public void compileContentWithGrammar(String content, String grammarName) {
    LoggingErrorListener lexicalAnalysisError = new LoggingErrorListener();
    LoggingErrorListener syntaxAnalysisError = new LoggingErrorListener();
    try {
      symbolTable =
          antlrFacade.compileContentWithGrammarST(content, grammarName, lexicalAnalysisError,
              syntaxAnalysisError);
      convert();
      lexicalGrammarErrors.addAll(lexicalAnalysisError.getErrorsList());
      syntaxGrammarErrors.addAll(syntaxAnalysisError.getErrorsList());
    } catch (Exception e) {
      journal.add(Level.ERROR, "N'arrive pas à compiler.", e.getMessage());
    }

  }

  public Journal getJournal() {

    for (String error : lexicalGrammarErrors) {
      journal.add(Level.ERROR, error);
    }
    for (String error : syntaxGrammarErrors) {
      journal.add(Level.ERROR, error);
    }

    return journal;
  }

  public String getContent() {
    return document.toString();
  }

  public Document getDocument() {
    return document;
  }

}
