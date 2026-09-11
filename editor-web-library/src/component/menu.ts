import { MenuManager, TabManager, CommandManager } from "ace-layout";
import { Popup } from "./popup";
import { Layout } from ".././layout/layout-type";
import { atelierController, atelierView } from ".././playground";
import { Etape } from ".././editor/enum";
import { dummyLogger, Logger } from "ts-log";

//Est-ce que je passe la vue en param ou j'y accede par une var
//provenant de playground ou atelier controller.

let log: Logger = console;

export class Menu {

  public constructor() {
    log.debug("Menu");
  }

  public createMenu() {
    log.debug(`addMenu`);
    let menuManager = MenuManager.getInstance();
    let position = 0;
    let root = "Atelier";
    menuManager.addByPath(root, { position: 0 });
    position = 0;
    menuManager.addByPath(root + "/Créer", {
      position: 0,
      exec: function() {
        atelierView.popup.newAtelier();
      },
    });
    menuManager.addByPath(root + "/Continuer", {
      exec: function() {
        atelierView.popup.findAtelier();
      },
    });

    menuManager.addByPath(root + "/Modifier", {
      exec: function() {
        atelierView.popup.modifyAtelier();
      },
    });

    menuManager.addByPath(root + "/Supprimer", {
      position: 0,
      exec: function() {
        //atelier.fromServer();
      },
    });

    menuManager.addByPath(root + "/Persister", {
      position: 0,
      exec: function() {
        //atelier.fromDb();
      },
    });

    root = "Vue";
    menuManager.addByPath(root, { position: 1150 });

    menuManager.addByPath(root + "/Information", {
      position: 0,
      exec: function() {
        atelierView.popup.info();
      },
    });

    let toggle = () => TabManager.getInstance().containers["console"].toggleShowHide();
    menuManager.addByPath(root + "/Basculer la console", {
      position: 0,
      exec: toggle,
      hotKey: "F6"
    });

    CommandManager.registerCommands([{
      bindKey: {
        win: "F6",
        mac: "F6"
      },
      exec: toggle
    }]);
  }
}
