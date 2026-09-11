import { Logger } from "ts-log";
import { Etape, Etat, GroupUI } from "./enum";
import { Layout } from ".././layout/layout-type";
import { SetWithContentEquality } from ".././utils"

let log: Logger = console;

export class TabModel {
  contenu: string;
  etape: Etape;
  extension: string;
  form_ui: string;
  group_ui: GroupUI;
  id_tab: string;
  langage: string;
  titre: string;

  public constructor(
    contenu: string,
    etape: Etape,
    extension: string,
    form_ui: string,
    group_ui: GroupUI,
    id_tab: string,
    langage: string,
    titre: string,
  ) {
    this.contenu = contenu;
    this.etape = etape;
    this.extension = extension;
    this.form_ui = form_ui;
    this.group_ui = group_ui;
    this.id_tab = id_tab;
    this.langage = langage;
    this.titre = titre;
  }
}

export class AtelierModel {
  id_atelier: string;
  titre: string;
  config: string;
  date_create: Date | undefined;
  date_modification: Date | undefined;
  etat: Etat;
  intrant_db: Array<TabModel>;
  intrant_ui: Array<TabModel>;
  epurerResult: Array<TabModel>;
  formaliserResult: Array<TabModel>;
  presenterResult: Array<TabModel>;
  epurerConsole: Array<TabModel>;
  formaliserConsole: Array<TabModel>;
  presenterConsole: Array<TabModel>;
  atelierConsole: Array<TabModel>;

  public constructor() {
    this.id_atelier = "";
    this.titre = "";
    this.config = "";
    this.date_create = undefined;
    this.date_modification = undefined;
    this.etat = Etat.NotStarted;
    this.intrant_db = [];
    this.intrant_ui = [];
    this.epurerResult = [];
    this.formaliserResult = [];
    this.presenterResult = [];
    this.epurerConsole = [];
    this.formaliserConsole = [];
    this.presenterConsole = [];
    this.atelierConsole = [];
  }



  /* Bof ce n'est pas un vrai cast de newAtelier vers AtelierModel. */
  public update(newAtelier) {
    if (!newAtelier.error) {
      this.id_atelier = newAtelier.id_atelier;
      this.titre = newAtelier.titre;
      this.config = newAtelier.config;
      this.date_create = newAtelier.date_create;
      this.date_modification = newAtelier.date_modification;
      this.etat = Etat.NotStarted.get(newAtelier.etat);
      this.intrant_db = newAtelier.intrant_db;
      this.intrant_ui = newAtelier.intrant_ui;
      this.epurerResult = newAtelier.epurerResult;
      this.formaliserResult = newAtelier.formaliserResult;
      this.presenterResult = newAtelier.presenterResult;
      this.epurerConsole = newAtelier.epurerConsole;
      this.formaliserConsole = newAtelier.formaliserConsole;
      this.presenterConsole = newAtelier.presenterConsole;
      this.atelierConsole = newAtelier.atelierConsole;
    }
  }

  /* Regarder plus tard pour changer l'option par default Etape et Layout. */
  public reset() {
    this.id_atelier = "";
    this.titre = "";
    this.config = "";
    this.date_create = undefined;
    this.date_modification = undefined;
    this.etat = Etat.NotStarted;
    this.intrant_db = [];
    this.intrant_ui = [];
    this.epurerResult = [];
    this.formaliserResult = [];
    this.presenterResult = [];
    this.epurerConsole = [];
    this.formaliserConsole = [];
    this.presenterConsole = [];
    this.atelierConsole = [];
  }

}