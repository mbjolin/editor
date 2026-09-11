import * as twoColumnsBottomJson from "./two-columns-bottom.json";
import * as threeRowsJson from "./three-rows.json";
import * as singleJson from "./single.json";
import * as threeColumnsJson from "./three-columns.json";

//https://stackoverflow.com/questions/41179474/use-object-literal-as-typescript-enum-values
export class Layout {
  static readonly Single = new Layout('Single', 'Une ligne', singleJson);
  static readonly TwoColumnsBottom = new Layout('TwoColumnsBottom', 'Deux colonnes en bas', twoColumnsBottomJson);
  static readonly ThreeRowsJson = new Layout('ThreeRowsJson', 'Trois lignes', threeRowsJson);
  static readonly ThreeColumns = new Layout('ThreeColumns', 'Trois colonnes', threeColumnsJson);
  static readonly Aucune = new Layout('Aucune', 'Aucune', singleJson);
  static readonly toList = [Layout.Single, Layout.TwoColumnsBottom, Layout.ThreeRowsJson, Layout.ThreeColumns];

  // private to disallow creating other instances of this type
  private constructor(private readonly key: string, public readonly text: string, public readonly form: any) {
  }

  public get(key: string) {
    switch (key) {
      case 'Single': return Layout.Single
      case 'TwoColumnsBottom': return Layout.TwoColumnsBottom
      case 'ThreeRowsJson': return Layout.ThreeRowsJson
      case 'ThreeColumns': return Layout.ThreeColumns
    }
    return Layout.Aucune;
  }

  public toJSON() {
    return this.key;
  }

  public toString() {
    return this.key;
  }
}
