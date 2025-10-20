import { computed, Injectable } from '@angular/core';
import SignalStore              from './signal-store';

export interface UserState {
  id: string;
  email: string;
  username: string;
}

const initialState : UserState  = {
    id: '',
    email: '',
    username: ''
}
@Injectable({ providedIn: 'root' })
export class UserStore extends SignalStore<UserState> {

  constructor() {
    super(initialState);
  }

  login(user: UserState) {
    this.patchState(user);
  }

  logout() {
    this.patchState(initialState);
  }

  isLoggedIn = computed(() => !!this.state.id);
}
