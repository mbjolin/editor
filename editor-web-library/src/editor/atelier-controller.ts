import { Logger } from "ts-log";
import { Etape, Etat, GroupUI } from "./enum";
import { Layout } from ".././layout/layout-type";
import { editorService } from "./editor-backend"
import { AtelierModel, TabModel } from "./atelier-model"
import { AtelierView } from "./atelier-view";
import { SetWithContentEquality } from ".././utils"
import { atelierModel } from "../playground";

let log: Logger = console;

export class AtelierController {

  private atelierModel: AtelierModel;
  private atelierView: AtelierView;

  public constructor(atelierModel: AtelierModel, atelierView: AtelierView) {
    this.atelierModel = atelierModel;
    this.atelierView = atelierView;
  }

  public async createOrModifyAtelier(titre: string, config: string) {
    log.debug("createAtelier ", titre);
    this.atelierModel.reset();
    this.atelierModel.titre = titre;
    this.atelierModel.config = config;
    this.saveAtelier();
  }

  public async restoreAtelier(id: string) {
    log.debug("restoreAtelier ", id);
    const response = await editorService.getAtelier(id) as AtelierModel;
    this.atelierModel.update(response);
    this.restoreModel();
    this.atelierView.refreshInfo();
  }

  public async saveAtelier() {
    log.debug("saveAtelier, ", this.atelierModel.titre, this.atelierModel.id_atelier);
    this.saveModel();
    const response = await editorService.saveAtelier(JSON.stringify(this.atelierModel)) as AtelierModel;
    this.atelierModel.update(response);
    this.restoreModel();
    this.atelierView.refreshInfo();
  }

  public changeConfig(config: string) {
    log.debug("changeConfig");
    this.atelierModel.config = config;
  }

  public addTab(nom: string, group: GroupUI, content?: string) {
    this.atelierView.createTab(nom, group, content)
    this.atelierView.createButton();
  }

  public async execute() {
    log.debug("execute");
    this.saveModel();
    const response = await editorService.execute(JSON.stringify(this.atelierModel)) as AtelierModel;
    this.atelierModel.update(response);
    this.restoreModel();
    this.atelierView.refreshInfo();
  }

  /* Il va y avoir un bug lorsqu'on split, les tabs reviennent dans le main. 
       Je trouve que c'est moins compliquer que de bien gérer le split ou de
       retirer la fonctionnalité. C'est compliquer d'aller chercher les tabs dans
       des containers-pan non identifié et imbriqué. */
  public saveModel() {
    log.debug("saveModel");
    let tabs = this.atelierView.getTabs();
    this.atelierModel.intrant_ui = tabs;
  }

  public restoreModel() {
    log.debug("restoreModel");

    this.atelierView.refreshState(Layout.Single);

    let tabs = [] as Array<TabModel>;
    tabs = tabs.concat(this.atelierModel.intrant_ui);
    tabs = tabs.concat(this.atelierModel.intrant_db);
    tabs = tabs.concat(this.atelierModel.epurerConsole);
    tabs = tabs.concat(this.atelierModel.epurerResult);
    tabs = tabs.concat(this.atelierModel.formaliserConsole);
    tabs = tabs.concat(this.atelierModel.formaliserResult);
    tabs = tabs.concat(this.atelierModel.presenterConsole);
    tabs = tabs.concat(this.atelierModel.presenterResult);
    tabs = tabs.concat(this.atelierModel.atelierConsole);

    if (tabs) {
      for (let tab of tabs) {
        this.atelierView.createTabWithModel(tab)
      }
    }
    this.atelierView.createButton();
  }

}