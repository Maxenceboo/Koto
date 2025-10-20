import { signal, Signal, WritableSignal } from '@angular/core';
import { Observable } from 'rxjs';

/**
 * Construit un type WritableSignalState à partir de State
 */
export type WritableSignalState<State> = {
  [Property in keyof State]?: WritableSignal<State[Property]>;
};

/**
 * Construit un type ReadOnlySignalState à partir de State
 */
export type ReadOnlySignalState<State> = {
  [Property in keyof State]?: Signal<State[Property]>;
};

class SignalStore<State> {

  // Etat initial à utiliser en interne lors de l'initialisation du store mais aussi lors du reset
  // Signaux computed exposés pour la lecture
  readonly state: ReadOnlySignalState<State> = {};
  // Cet etat ne doit pas être modifiée
  private readonly _initialState: State;
  // Signaux internes pour l'écriture
  private readonly _state: WritableSignalState<State> = {};

  constructor(initialState: State) {
    // Deep copy pour ne pas modifier l'état passée en paramètre
    this._initialState = structuredClone(initialState);
    this.buildState();
  }

  /**
   * Sets values for multiple properties on the store
   * This is used when there is a need to update multiple
   * properties in the store
   *
   * @param partialState - the partial state that includes
   *                      the new value to be saved
   */
  public patchState(partialState: Partial<State>): void {
    for (const key in partialState) {
      if (Object.prototype.hasOwnProperty.call(this._state, key)) {
        this._state[key]?.set(partialState[key]!);
      }
    }
  }

  /**
   * Applique la méthode "updater" au signal correspondant à "key".
   * @param key
   * @param updater
   */
  public update<StateProperty extends keyof State>(key: StateProperty, updater: (curr: State[StateProperty]) => State[StateProperty]) {
    if (Object.prototype.hasOwnProperty.call(this._state, key)) {
      this._state[key]?.update((currentValue) => updater(currentValue));
    }
  }

  /**
   * Souscrit à l'observable passé en paramètre et met à jour le signal correspondant à "key" à chaque nouvelle valeur émise.
   * Permet de créer un pont entre un observable (source) et une propriété du state (target)
   * @param key
   * @param obs
   */
  public subscribePropertyToObservable<StateProperty extends keyof State>(key: StateProperty, obs: Observable<State[StateProperty]>) {
    if (Object.prototype.hasOwnProperty.call(this._state, key)) {
      obs.subscribe(
        (data) => {
          this._state[key]?.update((currentValue) => {
            return {...currentValue, ...data};
          });
        }
      );
    }
  }

  /**
   * Reconstruit l'état interne du store à partir de l'état initiale d'origine.
   * Les signaux ne sont pas détruits/reconstruits seules leurs valeurs sont reset.
   */
  public reset() {
    for (const key in this._initialState) {
      this._state[key]?.set(this._initialState[key]);
    }
  }

  /**
   * Pour chaque propriété racine de l'état initiale on génère :
   *  - un WritableSignal pour l'écriture (dans _state)
   *  - Un Signal pour la lecture qui est le résultat de asReadOnly du signal en écriture (dans state)
   * @private
   */
  private buildState() {
    // Construction des signaux d'écriture et de lecture
    for (const key in this._initialState) {
      // Pour l'écriture
      // structuredClone pour deep-copy la valeur de la propriété
      this._state[key] = signal(structuredClone(this._initialState[key]));
      // Pour la lecture
      this.state[key] = this._state[key].asReadonly();
    }
  }
}

export default SignalStore;
