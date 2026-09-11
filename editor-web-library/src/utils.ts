import { dummyLogger, Logger } from "ts-log";

//https://www.npmjs.com/package/ts-log
let log: Logger = dummyLogger;

export function request(url) {
  let req: XMLHttpRequest;
  return new Promise(
    function(callback, error) {
      req = new XMLHttpRequest();
      req.onreadystatechange = function() {
        if (req.readyState === 4) {
          if ((req.status >= 200 && req.status < 300) || req.status === 1223) {
            callback(req);
          } else {
            error(req);
          }
          req.onreadystatechange = function() {
          };
        }
      };

      req.open('GET', url);
      req.responseType = '';

      req.send(null);
    }
  );
}


export function pathToTitle(path) {
  return path
    .replaceAll("-", " ");
}

//https://howtodoinjava.com/typescript/sets/
export class SetWithContentEquality<T> {
  private items: T[] = [];
  private getKey: (item: T) => string;

  constructor(getKey: (item: T) => string) {
    log.debug("SetWithContentEquality");
    this.getKey = getKey;
  }

  add(item: T): void {
    const key = this.getKey(item);
    if (!this.items.find((element) => this.getKey(element) === key)) {
      this.items.push(item);
    } else {
      this.items = this.items.filter((element) => this.getKey(element) !== key)
      this.items.push(item);
    }
  }

  remove(item: T): void {
    log.debug("remove before", item, this.items.length);
    const key = this.getKey(item);
    this.items = this.items.filter((element) => this.getKey(element) !== key)
    log.debug("remove after", item, this.items.length);
  }

  has(item: T): boolean {
    return this.items.some(existing => this.getKey(existing) === this.getKey(item));
  }

  values(): T[] {
    return [...this.items];
  }

  setValues(items: T[]) {
    this.items = items;
  }
}

//https://decipher.dev/30-seconds-of-typescript/docs/debounce/
export const debounce = (fn: Function, ms = 300) => {
  let timeoutId: ReturnType<typeof setTimeout>;
  return function (this: any, ...args: any[]) {
    clearTimeout(timeoutId);
    timeoutId = setTimeout(() => fn.apply(this, args), ms);
  };
};