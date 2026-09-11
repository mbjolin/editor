package ca.mbjolin.editor.process._2_intermediaterepresentation.document;

import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Etiquette;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Identifiant;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Valeur;

public record Meta (Identifiant identifiant, Etiquette etiquette,Valeur valeur) {

}
