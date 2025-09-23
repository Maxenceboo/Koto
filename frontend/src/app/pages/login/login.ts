import { Component } from '@angular/core';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { environment} from '../../../../environements/environement';
import { LoginForm } from './components/login-form/login-form';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [HttpClientModule, LoginForm],
  templateUrl: './login.html',
})
export class Login {
  constructor(private http: HttpClient) {}

  onLogin(payload: { email: string; password: string }) {
    this.http.post(`${environment.apiBase}/auth/login`, {
      email: payload.email,
      password: payload.password,
    }).subscribe({
      next: () => alert('Connecter !'),
      error: (e) => alert(e?.error?.message ?? 'Erreur lors de la connection'),
    });
  }
}
