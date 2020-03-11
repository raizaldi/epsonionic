import { WebPlugin } from '@capacitor/core';
import { epsonionicPlugin } from './definitions';

export class epsonionicWeb extends WebPlugin implements epsonionicPlugin {
  constructor() {
    super({
      name: 'epsonionic',
      platforms: ['web']
    });
  }

  async echo(options: { value: string }): Promise<{value: string}> {
    console.log('ECHO', options);
    return options;
  }
}

const epsonionic = new epsonionicWeb();

export { epsonionic };

import { registerWebPlugin } from '@capacitor/core';
registerWebPlugin(epsonionic);
