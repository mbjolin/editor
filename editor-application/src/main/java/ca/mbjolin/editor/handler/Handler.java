package ca.mbjolin.editor.handler;

import ca.mbjolin.editor.model.SimpleSession;

public abstract class Handler {

  protected Handler next;
  public abstract Boolean process(SimpleSession simpleSession);
  public abstract void setNext(Handler next);
}
