import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { UserStore }           from '../store/user.store';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private userStore: UserStore, private router: Router) {}

  canActivate(): boolean {
    if (!this.userStore.isLoggedIn()) {
      this.router.navigate(['/login']);
      return false;
    }
    return true;
  }
}
