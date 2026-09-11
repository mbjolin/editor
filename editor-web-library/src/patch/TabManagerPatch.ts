import { Ace, TabManager, Pane, EditorType, Tab } from "ace-layout";
import { EditSession } from "ace-layout/widgets/widget";

export interface TabPanelOptions {
  title: string;
  active?: boolean;
  icon?: string;
}

export interface TabOptions extends TabPanelOptions {
  preview?: boolean;
  path: string;
  editorType?: EditorType;
}

var tabCounterPatch = 0;

export function TabManagerPatch() {
  
  TabManager.prototype.addNewTab = function(pane: Pane, options?: TabOptions) {

    tabCounterPatch++;

    options ??= { title: `Untitled ${tabCounterPatch}`, path: tabCounterPatch.toString() };
    options.active = true;

    let newTab = pane.tabBar.addTab(new Tab(options));
    this.tabs[options.path] = newTab;
    
    return newTab;
  }

  TabManager.prototype.getTab = function <SessionType extends EditSession>(path: string): Tab<SessionType> {
    return this.tabs[path] as Tab<SessionType>;
  }
}
