import { Ace, AceLayout, EditorType, Box, Tab, MenuToolbar, TabManager } from "ace-layout";
import { Etape, Etat, GroupUI } from ".././editor/enum";
import { Button } from ".././component/button";
import { Info } from ".././component/info";
import { Menu } from ".././component/menu";
import { Popup } from ".././component/popup";
import { Logger } from "ts-log";
import { AtelierModel, TabModel } from "./atelier-model";
import { Layout } from ".././layout/layout-type";
import { SetWithContentEquality } from ".././utils"
let log: Logger = console;

export class AtelierView {

  masterBox: Box;
  mainBox: Box;
  resultBox: Box;
  consoleBox: Box;
  tabManager: TabManager;

  info: Info;
  menu: Menu;
  popup: Popup;

  public constructor(masterBox: Box, mainBox: Box,
    resultBox: Box, consoleBox: Box) {
    this.masterBox = masterBox;
    this.mainBox = mainBox;
    this.resultBox = resultBox;
    this.consoleBox = consoleBox;
  }

  /* On ne peut pas passer le tabManager dans le constructeur puisque le playground exige
  d'initialiser le menu avant de faire le render. */
  public setTabManager(tabManager: TabManager) {
    this.tabManager = tabManager;
  }

  public createButton() {
    let button = new Button()
    let mainButton = button.createMainButton();
    let resultButton = button.createResultButton();
    this.mainBox.setButtons([mainButton]);
    this.resultBox.setButtons([resultButton]);
  }

  public createInfo() {
    if (!this.info) {
      this.info = new Info(".menuToolBar");
      this.info.createInfo();
      this.info.refreshInfo();
    }
  }

  public refreshInfo() {
    this.info.refreshInfo();
  }

  public createPopup() {
    log.debug("createPopup");
    if (!this.popup) {
      this.popup = new Popup();
    }
  }

  public createMenu() {
    log.debug("createMenu");
    if (!this.menu) {
      this.menu = new Menu();
      this.menu.createMenu();
    }
  }

  public createTab<SessionType extends Ace.EditSession>(nom: string, group: GroupUI, content?: string) {
    log.debug(`createTab : `, nom, group);
    let mainPan = this.tabManager.getContainerPane(group);

    let tab = this.tabManager.addNewTab(mainPan, {
      title: nom,
      path: nom,
      editorType: EditorType.ace
    }) as Tab<SessionType>;

    if (content) {
      tab.session.setValue(content);
    }
  }

  public createTabWithModel<SessionType extends Ace.EditSession>(tab: TabModel) {
    log.debug(`createTab : `);
    let pan = this.tabManager.getContainerPane(tab.group_ui);
    if (tab.form_ui) {
      let newTab = this.tabManager.addNewTab(pan, JSON.parse(tab.form_ui)) as Tab<SessionType>;
      newTab.session.setValue(tab.contenu);
    } else {
      this.createTab(tab.titre, tab.group_ui, tab.contenu);
    }
  }

  public refreshState(layout: Layout) {
    this.tabManager.setState({ "main": layout });
  }

  /* Il va y avoir un bug lorsqu'on split, les tabs reviennent dans le main. 
        Je trouve que c'est moins compliquer que de bien gérer le split ou de
        retirer la fonctionnalité. C'est compliquer d'aller chercher les tabs dans
        des containers-pan non identifié et imbriqué. */
  public getTabs<SessionType extends Ace.EditSession>() {
    log.debug("getTabs");

    let result: Array<TabModel> = [];

    let map = new Map(Object.entries(this.tabManager.getTabs()));

    const consoleTabs = this.tabManager.getContainerPane(GroupUI.Console).tabBar.tabList as Tab<SessionType>[];
    for (let tab of consoleTabs) {
      if (tab.session) {
        map.delete(tab.path);
      }
    }
    const resultTabs = this.tabManager.getContainerPane(GroupUI.Result).tabBar.tabList as Tab<SessionType>[];
    for (let tab of resultTabs) {
      if (tab.session) {
        map.delete(tab.path);
      }
    }
    let tabs = Array.from(map.values()) as Tab<SessionType>[];
    for (let tab of tabs) {
      if (tab.session) {
        let titleArray = tab.title.split(".");
        let title = titleArray[0]
        let extension = "*"
        if (titleArray.length > 1) {
          extension = titleArray[1]
          title = tab.title  //On mets l'extesion dans le titre bof asccidoc include
        }
        
        let tabModel = new TabModel(
          tab.session.getValue(),
          Etape.Epurer,
          extension,
          JSON.stringify(tab.toJSON()),
          GroupUI.Main,
          "",
          "",
          title
        );
        result.push(tabModel);
      }
    }
    return result;
  }

}

