
export class Etape {
  static readonly Epurer = new Etape('EPURER', 'Epurer', 'macro');
  static readonly Formaliser = new Etape('FORMALISER', 'Formaliser', 'document');
  static readonly Presenter = new Etape('PRESENTER', 'Présenter', 'règle');
  static readonly Aucune = new Etape('Aucune', 'aucune', 'règle');
  static readonly toList = [Etape.Epurer, Etape.Formaliser, Etape.Presenter];

  // private to disallow creating other instances of this type
  private constructor(public readonly key: string, public readonly text: string, public readonly langagefonction: string) { }

  public get(key: string) {
    switch (key) {
      case 'EPURER': return Etape.Epurer
      case 'FORMALISER': return Etape.Formaliser
      case 'PRESENTER': return Etape.Presenter
    }
    return Etape.Aucune;
  }

  public toJSON() {
    return this.key;
  }

  public toString() {
    return this.key;
  }
}

export class Etat {
  static readonly NotStarted = new Etat('NOT_STARTED', 'Pas démarré');
  static readonly EpurerSuccess = new Etat('EPURER_SUCCESS', 'Succès epurer');
  static readonly EpurerFail = new Etat('EPURER_FAIL', 'Échec epurer');
  static readonly FormaliserSuccess = new Etat('FORMALISER_SUCCESS', 'Succès formaliser');
  static readonly FormaliserFail = new Etat('FORMALISER_FAIL', 'Échec formaliser');
  static readonly FormaliserRuleSuccess = new Etat('FORMALISER_RULE_SUCCESS', 'Succès formaliser');
  static readonly FormaliserRuleFail = new Etat('FORMALISER_RULE_FAIL', 'Échec formaliser');
  static readonly PresenterSuccess = new Etat('PRESENTER_SUCCESS', 'Succès présenter');
  static readonly PresenterFail = new Etat('PRESENTER_FAIL', 'Échec présenter');
  static readonly AtelierSuccess = new Etat('ATELIER_SUCCESS', 'Succès atelier');
  static readonly AtelierFail = new Etat('ATELIER_FAIL', 'Échec atelier');
  static readonly Finish = new Etat('Finish', 'Atelier terminé');

  // private to disallow creating other instances of this type
  private constructor(public readonly key: string, public readonly text: string) { }

  public get(key: string) {
    switch (key) {
      case 'NOT_STARTED': return Etat.NotStarted
      case 'EPURER_SUCCESS': return Etat.EpurerSuccess
      case 'EPURER_FAIL': return Etat.EpurerFail
      case 'FORMALISER_SUCCESS': return Etat.FormaliserSuccess
      case 'FORMALISER_FAIL': return Etat.FormaliserFail
      case 'FORMALISER_RULE_SUCCESS': return Etat.FormaliserRuleSuccess
      case 'FORMALISER_RULE_FAIL': return Etat.FormaliserRuleFail
      case 'PRESENTER_SUCCESS': return Etat.PresenterSuccess
      case 'PRESENTER_FAIL': return Etat.PresenterFail
      case 'ATELIER_SUCCESS': return Etat.AtelierSuccess
      case 'ATELIER_FAIL': return Etat.AtelierFail
      case 'FINISH': return Etat.Finish
    }
    return Etat.NotStarted;
  }

  public toJSON() {
    return this.key;
  }

  public toString() {
    return this.key;
  }
}


export enum GroupUI {
  Main = "main",
  Result = "result",
  Console = "console"
};