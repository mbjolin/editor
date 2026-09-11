import { AceLayout, Box, MenuToolbar, TabManager } from "ace-layout";
import { Etape} from "./editor/enum";
import { Logger } from "ts-log";
import { TabManagerPatch } from "./patch/TabManagerPatch";
import { AtelierController } from "./editor/atelier-controller";
import { AtelierView } from "./editor/atelier-view"
import { AtelierModel} from "./editor/atelier-model"
import { Layout } from "./layout/layout-type"
import "./custom.css"

TabManagerPatch();

//https://www.npmjs.com/package/ts-log
let log: Logger = console;

let masterBox: Box, mainBox: Box, resultBox: Box, consoleBox: Box;

function initBox() {
  masterBox = new Box({
    toolBars: {
      top: new MenuToolbar(),
    },
    vertical: false,
    0: new Box({
      vertical: true,
      0: new Box({
        0: mainBox = new Box({ isMain: true }),
        1: resultBox = new Box({ isMain: true }),
      }),
      1: new Box({
        0: consoleBox = new Box({
          size: 100,
          isMain: true,
        }),
      })
    })
  });
  
  return [masterBox, mainBox, resultBox, consoleBox];
}

[masterBox, mainBox, resultBox, consoleBox] = initBox();

document.body.innerHTML = "";

new AceLayout(masterBox, ".tabPlusButton {display: none !important;}");

export var atelierModel = new AtelierModel();
export var atelierView = new AtelierView(masterBox, mainBox, resultBox, consoleBox);
atelierView.createPopup();
atelierView.createMenu();

masterBox.render();

document.body.appendChild(masterBox.element);

function onResize() {
  masterBox.setBox(0, 0, window.innerWidth, window.innerHeight);
}

let tabManager = TabManager.getInstance({
  containers: {
    main: mainBox,
    result: resultBox,
    console: consoleBox,
  }
});

let tabState = { "main": Layout.Single.form };
tabManager.setState(tabState);

atelierView.setTabManager(tabManager);

atelierView.createButton();
atelierView.createInfo()

export var atelierController = new AtelierController(atelierModel, atelierView);

onResize();

window.onresize = onResize;
