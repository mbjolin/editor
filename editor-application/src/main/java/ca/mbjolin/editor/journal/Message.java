package ca.mbjolin.editor.journal;

import java.time.LocalDateTime;

import org.slf4j.event.Level;

public class Message {

  private Level level;

  private String message;

  private LocalDateTime currentTime;

  private String stacktrace;

  public Message(Level level, String message) {
    this.level = level;
    this.message = message;
    this.currentTime = LocalDateTime.now();
  }

  public Message(Level level, String message, String stacktrace) {
    this.level = level;
    this.message = message;
    this.stacktrace = stacktrace;
    this.currentTime = LocalDateTime.now();
  }

  public Level getLevel() {
    return level;
  }

  public String toString() {
    String result;

    if (stacktrace != null && !stacktrace.isEmpty()) {
      result = new StringBuilder()
          .append(currentTime.toString().substring(0, 22))
          .append(" - ")
          .append(level.toString())
          .append(" - ")
          .append(message)
          .append("[")
          .append(stacktrace)
          .append("]")
          .toString();
    } else if(level!=null) {
      result = new StringBuilder()
          .append(currentTime.toString().substring(0, 22))
          .append(" - ")
          .append(level.toString())
          .append(" - ")
          .append(message)
          .toString();
    }
    else {
      result = new StringBuilder()
          .append(currentTime.toString().substring(0, 22))
          .append(" - ")
          .append(message)
          .toString();
    }
    return result;
  }
}
