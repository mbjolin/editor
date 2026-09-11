import { dummyLogger, Logger } from "ts-log";
import Swal from 'sweetalert2'

//https://www.npmjs.com/package/ts-log
let log: Logger = console;


export class EditorService {
  private url;
  public constructor(url: string) {
    this.url = url;
  }

  public async saveModel(model: string) {
    log.debug(`saveModel `, model);
    let data;
    try {
      const res = await fetch("https://localhost:8443/atelier/update", {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: model
      });
      data = await res.json();
    } catch (error) {
      log.error(error);
    }

    return data;
  }

  public async getModel(id: string) {
    log.debug(`getModel : ${id}`, id);
    let data;
    try {
      const res = await fetch("https://localhost:8443/atelier/read", {
        method: "GET",
        headers: { 'Content-Type': 'application/json' },
        body: id
      });
      data = await res.json();
    } catch (error) {
      log.error(error);
    }

    return data;
  }

  public async syncServerToDb(id: string) {
    log.debug(`syncServerToDb : ${id}`, id);
    let data;
    try {
      const res = await fetch("https://localhost:8443/atelier/syncServerToDb", {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: id
      });
      data = await res.json();
    } catch (error) {
      log.error(error);
    }

    return data;
  }

  public async syncDbToServer(id: string) {
    log.debug(`syncDbToServer : ${id}`, id);
    let data;
    try {
      const res = await fetch("https://localhost:8443/atelier/syncServerToDb", {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: id
      });
      data = await res.json();
    } catch (error) {
      log.error(error);
    }

    return data;
  }

  public async getLangage(fonction: string) {
    log.debug(`getLangage : ${fonction}`, fonction);
    let langages = await this.getAllLangage();

    return langages[fonction];
  }

  public async getAllLangage() {
    log.debug(`getAllLangage`);
    let data;
    try {
      const res = await fetch("https://localhost:8443/langage/getall", {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: ""
      });
      data = await res.json();
    } catch (error) {
      log.error(error);
    }

    return data;
  }

  //https://stackoverflow.com/questions/41103360/how-to-use-fetch-in-typescript
  public async presentation(input: string) {
    log.debug(`presentation : ${input}`, input);
    const res = await fetch("https://localhost:8443/document/update", {
      method: "POST",
      headers: { 'Content-Type': 'application/json' },
      body: input
    });

    const { data, errors } = await res.json();

    if (errors) {
      log.error(errors.message);
    }

    return data;
  }

  public async searchIntrant(type: string, nom: string) {
    log.debug(`search : ${type} ${nom}`);
    const url = new URL(this.url + '/intrant/search');
    const httpOptions = {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    };

    url.searchParams.set('q', nom);
    url.searchParams.set('t', type);
    let result;

    try {
      const res = await fetch(url, httpOptions);

      result = await res.json();
    } catch (error) {
      log.error(error);
    }

    return result;
  }

  public async getIntrantList(type: string, number: string) {
    log.debug(`getIntrantList : ${type} ${number}`);
    const url = new URL(this.url + '/intrant/last');
    const httpOptions = {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    };

    url.searchParams.set('n', number);
    url.searchParams.set('t', type);
    let result;

    try {
      const res = await fetch(url, httpOptions);

      result = await res.json();
    } catch (error) {
      log.error(error);
    }

    log.debug(`search exit : ${result}`, result);
    return result;
  }

  public async getIntrant(id: string) {
    log.debug(`getIntrant : ${id}`);
    const url = new URL(this.url + '/intrant');
    const httpOptions = {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    };

    url.searchParams.set('id', id);
    let result;

    try {
      const res = await fetch(url, httpOptions);

      result = await res.json();
    } catch (error) {
      log.error(error);
    }

    return result;
  }

  public async searchAtelier(nom: string) {
    log.debug(`searchAtelier : ${nom}`);
    const url = new URL(this.url + '/atelier/search');
    const httpOptions = {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    };

    url.searchParams.set('q', nom);
    let result;

    try {
      const res = await fetch(url, httpOptions);

      result = await res.json();
    } catch (error) {
      log.error(error);
    }

    return result;
  }

  public async getAtelierList(number: string) {
    log.debug(`getAtelierList : ${number}`);
    const url = new URL(this.url + '/atelier/last');
    const httpOptions = {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    };

    url.searchParams.set('n', number);
    let result;

    try {
      const res = await fetch(url, httpOptions);

      result = await res.json();
    } catch (error) {
      log.error(error);
    }

    log.debug(`search exit : ${result}`, result);
    return result;
  }

  public async getAtelier(id: string) {
    log.debug(`getAtelier : ${id}`);
    const url = new URL(this.url + '/atelier');
    const httpOptions = {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
    };

    url.searchParams.set('id', id);
    let result;

    try {
      const res = await fetch(url, httpOptions);

      result = await res.json();
    } catch (error) {
      log.error(error);
    }

    return result;
  }

  public async saveAtelier(model: string) {
    log.debug(`saveAtelier`);
    let data;
    try {
      const res = await fetch("https://localhost:8443/atelier/updateString", {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: model
      });
      data = await res.json();
      if (data.error) {
        log.error(data.error);
      }
    } catch (error) {
      log.error(error);
    }

    return data;
  }

  public async verifyConfigAtelier(model: string) {
    log.debug(`saveAtelier`);
    let data;
    try {
      const res = await fetch("https://localhost:8443/atelier/verifyConfig", {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: model
      });
      data = await res.json();
      if (data.error) {
        log.error(data.error);
      }
    } catch (error) {
      log.error(error);
    }

    return data;
  }

  public async execute(model: string) {
    log.debug(`execute`);
    const url = new URL(this.url + '/atelier/executeString');
    let data;
    try {
      const res = await fetch(url, {
        method: "POST",
        headers: { 'Content-Type': 'application/json' },
        body: model
      });
      data = await res.json();
      if (data.error) {
        log.error(data.error);
      }
    } catch (error) {
      log.error(error);
    }

    return data;
  }

}

export var editorService = new EditorService('https://localhost:8443');




