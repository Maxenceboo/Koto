import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../../environements/environement';

type AuthResponse = { token: string; userId: string; email: string; username: string };

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly TOKEN_KEY = 'auth_token';
  private readonly USER_KEY = 'auth_user';

  currentUser = signal<{ userId: string; email: string; username: string } | null>(this.loadUser());

  constructor(private http: HttpClient, private router: Router) {}

  private loadUser() {
    const raw = localStorage.getItem(this.USER_KEY);
    return raw ? (JSON.parse(raw)) : null;
  }

  get token(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    return !!this.token;
  }

  login(email: string, password: string) {
    return this.http.post<AuthResponse>(`${environment.apiBase}/auth/login`, { email, password });
  }

  register(email: string, username: string, password: string) {
    return this.http.post<AuthResponse>(`${environment.apiBase}/auth/register`, { email, usernameGlobal: username, password });
  }

  setSession(res: AuthResponse) {
    localStorage.setItem(this.TOKEN_KEY, res.token);
    const user = { userId: res.userId, email: res.email, username: res.username };
    localStorage.setItem(this.USER_KEY, JSON.stringify(user));
    this.currentUser.set(user);
  }

  logout() {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USER_KEY);
    this.currentUser.set(null);
    this.router.navigateByUrl('/login');
  }
}

