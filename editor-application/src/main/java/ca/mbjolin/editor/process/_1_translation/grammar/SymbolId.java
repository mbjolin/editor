package ca.mbjolin.editor.process._1_translation.grammar;

public record SymbolId (String idObject, String idLocal, String idGlobal) {

  public SymbolId(String idObject, String idLocal, String idGlobal) {
    assert idLocal.isEmpty() ^ idGlobal.isEmpty();
    this.idObject = idObject;
    this.idLocal = idLocal;
    this.idGlobal = idGlobal;
  }
}
