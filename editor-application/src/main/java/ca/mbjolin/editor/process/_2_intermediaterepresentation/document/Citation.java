package ca.mbjolin.editor.process._2_intermediaterepresentation.document;

import java.util.List;

import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Description;
import ca.mbjolin.editor.process._2_intermediaterepresentation.document.micro.Identifiant;

public record Citation (Identifiant identifiant, Description description, List<Source> sources) {

}
