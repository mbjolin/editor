package ca.mbjolin.editor.journal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.event.Level;

/* Serait intéressant d'avoir un arbre de journal. */
public class Journal {

  protected List<Message> messages = new ArrayList<>();
  
  public Journal() {}

  public Journal(String content) {
    String[] contentTab = content.split("\n");
    for(String row : contentTab) {
      messages.add(new Message(null, row));
    }
  }

  public void add(Level level, String content, String additionalContent) {
    messages.add(new Message(level, content, additionalContent));
  }
  
  public void add(Level level, String content) {
    messages.add(new Message(level, content));
  }
  
  public void concat(Journal journal) {
    messages.addAll(journal.getMessages());
  }

  public String getMessagesAsString() {
    String messageAsString =
        messages.stream()
            .map(message -> message.toString())
            .collect(Collectors.joining("\n"));
    return messageAsString;
  }
  

  public Boolean isWithoutError() {
    Boolean isWithoutError = true;

    for (Message message : messages) {
      if (message.getLevel() == Level.ERROR) {
        isWithoutError = false;
        break;
      }
    }

    return isWithoutError;
  }

  public List<Message> getMessages() {
    return messages;
  }
}
