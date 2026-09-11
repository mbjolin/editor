import {atelierModel} from ".././playground"
import { Etape, Etat } from ".././editor/enum";
import { dummyLogger, Logger } from "ts-log";

let log: Logger = console;

export class Info {
  private element: HTMLElement;

  public constructor(selector: string) {
    this.element = document.querySelector(selector) as HTMLElement;
    if (!this.element) {
      log.error("Il ne trouve pas le selector : " + selector);
    }
  }

  public refreshInfo() {
    log.debug("refreshInfo")
    let div = document.querySelector('#infoId');
    if (div) {
      div.innerHTML = this.createHtml(atelierModel.titre,
        atelierModel.etape_courante, atelierModel.date_modification, atelierModel.etat);

    }
  }

  public createInfo() {
    log.debug("createInfo")
    let div = document.createElement("div");
    div.id = "infoId";
    div.style.marginLeft = "auto";
    div.style.marginRight = "5px";
    div.style.color = "white";

    this.element.appendChild(div);
  }

  private createHtml(name: string | undefined, etape: Etape | undefined, lastModifyDate: Date | undefined, etat: Etat | undefined) {

    let showName = name ? name : "S.O.";
    let showStage = etape ? etape.text : "S.O.";
    let showLastModify = lastModifyDate ? lastModifyDate.toLocaleString() : "S.O.";
    let showState = etat ? etat.text : "S.O.";

    let html = `
    <table>
        <tr>
          <td>Atelier : ${showName} | </td>
          <td>Étape : ${showStage} | </td>
          <td>Dernière conservation : ${showLastModify} | </td>
          <td>État : ${showState}</td>
        </tr>
    </table>
    `
    return html;
  }

}


