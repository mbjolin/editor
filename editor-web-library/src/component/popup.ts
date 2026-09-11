//https://sweetalert2.github.io
import Swal from 'sweetalert2'
import { dummyLogger, Logger } from "ts-log";
import { atelierController, atelierModel } from ".././playground";
import { editorService } from ".././editor/editor-backend";
//https://github.com/select2/select2/issues/6081
//https://github.com/Choices-js/Choices
import Choices from 'choices.js';
import "choices.js/public/assets/styles/choices.css";
import { debounce } from '../utils';
import { Etape, GroupUI } from '../editor/enum';

let log: Logger = dummyLogger;

export class Popup {

  public constructor() { }

  public async newAtelier() {
    log.debug(`newAtelier`);
    type CustomFormResult = {
      nom: string,
      config: string
    }

    let nomInput: HTMLInputElement;
    let configInput: HTMLInputElement;

    const result = await Swal.fire<CustomFormResult>({
      title: 'Créer un atelier',
      html: 
`<input type="text" id="nom" class="swal2-input" placeholder="Nom">
<textarea id='textareaConfig' style='width: 340px; height: 150px;'>
Étape;Entrées;Sortie;Transformation
épuration;adoc;adoc;AsciidocMacro
épuration;rs;rs;Identity
formalisation;adoc;Document;Asciidoc
formalisation;rs;Document;RestructuredText
formalisation;Document;Document;Rule
présentation;Document;adoc;AdocPresentation
</textarea>
`,
      didOpen: () => {
        const popup = Swal.getPopup()!
        nomInput = popup.querySelector('#nom') as HTMLInputElement
        nomInput.onkeyup = (event) => event.key === 'Enter' && Swal.clickConfirm()

        configInput = popup.querySelector('#textareaConfig') as HTMLInputElement
      },

      preConfirm: async () => {
        const nom = nomInput.value
        const config = configInput.value
        if (!nom) {
          Swal.showValidationMessage(`Entrer un nom.`)
        }
        if (!config) {
          Swal.showValidationMessage(`Entrer une configuration.`)
        }

        const data = await editorService.verifyConfigAtelier(config);

        if (data.errors > 1) {
          Swal.showValidationMessage(`Problème de config : ` + data.errors)
        }
        return { nom, config }
      },
      confirmButtonText: 'Confirmer'
    });
    if (result.value != null) {
      await atelierController.createOrModifyAtelier(result.value?.nom, result.value?.config);
    }
  }


  public async findAtelier() {
    log.debug(`findAtelier`);
    type CustomFormResult = {
      atelier: string
    }

    var selecthtml = `<select id="selectfind" class="selectmax" type="text" id="nom" class="swal2-input" placeholder="Nom">`
    let selectInput: HTMLInputElement;

    const result = await Swal.fire<CustomFormResult>({
      title: 'Continue un atelier',
      html: `${selecthtml}`,
      didOpen: async () => {
        const popup = Swal.getPopup()!
        selectInput = popup.querySelector('#selectfind') as HTMLInputElement
        const element = document.querySelector('.selectmax');
        if (element) {
          const choices = new Choices(element);

          const data = await editorService.getAtelierList("5");
          choices.setChoices(data, "value", "label", false);

          //https://github.com/Choices-js/Choices/issues/914
          element.addEventListener(
            'search',
            debounce(async e => {
              const data = await editorService.searchAtelier(e.detail.value);
              console.log(data);
              choices.clearChoices();
              choices.setChoices(data, "value", "label", false);
            }, 400)
          )
        }
      },
      preConfirm: () => {
        const atelier = selectInput.value
        if (!atelier) {
          Swal.showValidationMessage(`Choisir un atelier.`)
        }
        return { atelier }
      },
      confirmButtonText: 'Confirmer',
    });

    if (result.value != null) {
      console.log(result.value?.atelier);
      atelierController.restoreAtelier(result.value?.atelier);
    }
  }

  public async modifyAtelier() {
    log.debug(`modifyAtelier`);
    type CustomFormResult = {
      nom: string,
      config: string
    }

    let nomInput: HTMLInputElement;
    let configInput: HTMLInputElement;

    const result = await Swal.fire<CustomFormResult>({
      title: "Modifier l'atelier ",
      html:
        `
        <input type="text" id="nom" class="swal2-input" placeholder="Nom">
        <textarea id='textareaConfig' style='width: 340px; height: 150px;'></textarea>
        `,
      didOpen: () => {
        const popup = Swal.getPopup()!
        nomInput = popup.querySelector('#nom') as HTMLInputElement
        nomInput.onkeyup = (event) => event.key === 'Enter' && Swal.clickConfirm()
        nomInput.value = atelierModel.titre

        configInput = popup.querySelector('#textareaConfig') as HTMLInputElement
        configInput.value = atelierModel.config
      },

      preConfirm: async () => {
        const nom = nomInput.value
        const config = configInput.value
        if (!nom) {
          Swal.showValidationMessage(`Entrer un nom.`)
        }
        if (!config) {
          Swal.showValidationMessage(`Entrer une configuration.`)
        }

        const data = await editorService.verifyConfigAtelier(config);

        if (data.error || data.errors > 1) {
          Swal.showValidationMessage(`Problème de config : ` + data.error + data.errors)
        }
        return { nom, config }
      },
      confirmButtonText: 'Confirmer'
    });
    if (result.value != null) {
      await atelierController.createOrModifyAtelier(result.value?.nom, result.value?.config);
    }

  }

  public async newIntrant() {
    log.debug(`newIntrant`);
    type CustomFormResult = {
      nom: string
    }

    let nomInput: HTMLInputElement;

    const result = await Swal.fire<CustomFormResult>({
      title: 'Crée un intrant ',
      html: `
          <input type="text" id="nom" class="swal2-input" placeholder="Nom">
        `,
      didOpen: () => {
        const popup = Swal.getPopup()!
        nomInput = popup.querySelector('#nom') as HTMLInputElement
        nomInput.onkeyup = (event) => event.key === 'Enter' && Swal.clickConfirm()
      },

      preConfirm: () => {
        const nom = nomInput.value
        if (!nom) {
          Swal.showValidationMessage(`Entrer un nom.`)
        }
        return { nom }
      },
      confirmButtonText: 'Confirmer'
    });
    if (result.value != null) {
      atelierController.addTab(result.value?.nom, GroupUI.Main);
    }
  }

  public async findIntrant() {
    log.debug(`findInput`);
    type CustomFormResult = {
      intrant: string
    }

    var selecthtml = `<select id="selectfind" class="selectmax" type="text" id="nom" class="swal2-input" placeholder="Nom">`
    let selectInput: HTMLInputElement;

    const result = await Swal.fire<CustomFormResult>({
      title: 'Ajoute un intrant ',
      html: `${selecthtml}`,
      didOpen: async () => {
        const popup = Swal.getPopup()!
        selectInput = popup.querySelector('#selectfind') as HTMLInputElement
        const element = document.querySelector('.selectmax');
        if (element) {
          const choices = new Choices(element);

          const data = await editorService.getIntrantList(Etape.Epurer.key, "5");
          choices.setChoices(data, "value", "label", false);

          //https://github.com/Choices-js/Choices/issues/914
          element.addEventListener(
            'search',
            debounce(async e => {
              const data = await editorService.searchIntrant(Etape.Epurer.key, e.detail.value);
              console.log(data);
              choices.clearChoices();
              choices.setChoices(data, "value", "label", false);
            }, 400)
          )
        }
      },
      preConfirm: () => {
        const intrant = selectInput.value
        if (!intrant) {
          Swal.showValidationMessage(`Choisir un intrant.`)
        }
        return { intrant }
      },
      confirmButtonText: 'Confirmer',
    });

    if (result.value != null) {
      console.log(result.value?.intrant);
      const data = await editorService.getIntrant(result.value?.intrant);
      atelierController.addTab(data["titre"], GroupUI.Main, data["contenu"]);
    }
  }

  public info() {
    log.debug(`info`);

    Swal.fire({
      imageUrl: "https://localhost:8443/static/image/SoftwareAide.png",
      imageAlt: "Image du flow",
      width: 2000,
      confirmButtonText: 'Fermer'
    });
  }

}

