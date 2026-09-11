import { dummyLogger, Logger } from "ts-log";
import { Etape } from ".././editor/enum"
import { atelierModel, atelierController, atelierView } from ".././playground"

let log: Logger = dummyLogger;

export class Button {

  public constructor() { }

  public creerMainButton() {
    log.debug(`creerMainButton`);

    async function call() {
      atelierView.popup.newIntrant();
    }

    let button = document.createElement("button");
    button.textContent = "créer";
    button.style.marginLeft = "auto";
    button.style.marginRight = "5px";
    button.setAttribute('title', "creer");
    button.onclick = call;
    return button;
  }

  public ajouterMainButton() {
    log.debug(`ajouterMainButton`);

    async function call() {
      atelierView.popup.findIntrant();
    }

    let button = document.createElement("button");
    button.textContent = "ajouter";
    button.style.marginLeft = "auto";
    button.style.marginRight = "5px";
    button.setAttribute('title', "ajouter");
    button.onclick = call;
    return button;
  }

  public executerMainButton() {
    log.debug(`executerMainButton`);

    async function call() {
      atelierController.execute();
    }

    let button = document.createElement("button");
    button.textContent = "exécuter";
    button.style.marginLeft = "auto";
    button.style.marginRight = "5px";
    button.setAttribute('title', "executer");
    button.onclick = call;
    return button;
  }


  public createMainButton() {

    let div = document.createElement("div");
    div.appendChild(this.creerMainButton());
    div.appendChild(this.ajouterMainButton());
    div.appendChild(this.executerMainButton());

    return div;
  }

  public epurerResultButton() {
    log.debug(`epurerResultButton`);

//    async function call() {
//      atelierController.changeEtape(Etape.Epurer);
//    }



    let button = document.createElement("button");
    button.textContent = "épuration";
    button.setAttribute('title', "epurationButton");
    button.style.border = "0px";

//    button.onclick = call;

//    if (atelierModel.etape_courante == Etape.Epurer) {
//      button.style.background = "grey";
//    }

    return button;
  }

  public formaliserResultButton() {
    log.debug(`formaliserResultButton`);

//    async function call() {
//      atelierController.changeEtape(Etape.Formaliser);
//    }

    let button = document.createElement("button");
    button.textContent = "formalisation";
    button.setAttribute('title', "formalisationButton");
    button.style.border = "0px";
//    button.onclick = call;

//    if (atelierModel.etape_courante == Etape.Formaliser) {
//      button.style.background = "grey";
//    }

    return button;
  }

  public presenterResultButton() {
    log.debug(`presenterResultButton`);

//    async function call() {
//      atelierController.changeEtape(Etape.Presenter);
//    }

    let button = document.createElement("button");
    button.textContent = "présentation";
    button.setAttribute('title', "presentationButton");
    button.style.border = "0px";
//    button.onclick = call;

//    if (atelierModel.etape_courante == Etape.Presenter) {
//      button.style.background = "grey";
//    }
    return button;
  }

  public createResultButton() {

    let div = document.createElement("div");
    let resultLabel = document.createElement("span");
    resultLabel.textContent = "(étape)"
    div.appendChild(this.epurerResultButton());
    div.appendChild(this.formaliserResultButton());
    div.appendChild(this.presenterResultButton());
    div.appendChild(resultLabel);

    return div;
  }

  public templateButton(textContent: string, title: string, onclick: () => void) {
    log.debug("templateButton")
    let button = document.createElement("button");
    button.textContent = textContent;
    button.style.marginLeft = "auto";
    button.style.marginRight = "5px";
    button.setAttribute('title', title);
    button.onclick = onclick;
    return button;
  }




  public cleanConsoleButton() {
    log.debug(`cleanConsoleButton`);

    async function call() {
      console.log("clear2");
    }

    let button = document.createElement("button");
    button.textContent = "nettoyer";
    button.style.marginLeft = "auto";
    button.style.marginRight = "5px";
    button.setAttribute('title', "nettoyer");
    button.onclick = call;
    return button;
  }

  public saveResultButton() {
    log.debug(`saveResultButton`);

    async function call() {
      console.log("clear2");
    }

    let button = document.createElement("button");
    button.textContent = "conserver";
    button.style.marginLeft = "auto";
    button.style.marginRight = "5px";
    button.setAttribute('title', "conserver");
    button.onclick = call;
    return button;
  }

  public cleanResultButton() {
    log.debug(`cleanResultButton`);

    async function call() {
      console.log("clear2");
    }

    let button = document.createElement("button");
    button.textContent = "nettoyer";
    button.style.marginLeft = "auto";
    button.style.marginRight = "5px";
    button.setAttribute('title', "nettoyer");
    button.onclick = call;
    return button;
  }

}


