import { Component } from '@angular/core';
import { Navbar } from '../../shared/ui/navbar/navbar';
import { RegisterForm } from './components/register-form/register-form';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { environment} from '../../../../environements/environement';

@Component({
  selector: 'app-register-page',
  standalone: true,
  imports: [HttpClientModule, Navbar, RegisterForm],
  templateUrl: './register.html',
})
export class Register {
  constructor(private http: HttpClient) {}

  onRegister(payload: { username: string; email: string; password: string }) {
    this.http.post(`${environment.apiBase}/auth/register`, {
      usernameGlobal: payload.username,
      email: payload.email,
      password: payload.password,
    }).subscribe({
      next: () => alert('Compte créé !'),
      error: (e) => alert(e?.error?.message ?? 'Erreur lors de l’inscription'),
    });
  }
}
