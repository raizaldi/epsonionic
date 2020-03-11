declare module "@capacitor/core" {
  interface PluginRegistry {
    epsonionic: epsonionicPlugin;
  }
}

export interface epsonionicPlugin {
  echo(options: { value: string }): Promise<{value: string}>;
}
