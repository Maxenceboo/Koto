import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../shared/services/auth.service';
import { LoginForm } from './components/login-form/login-form';
@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [LoginForm],
  templateUrl: './login.html',
})
export class Login {
  private auth = inject(AuthService);
  private router = inject(Router);

  onLogin(payload: { email: string; password: string }) {
    this.auth.login(payload.email, payload.password).subscribe({
      next: (res) => {
        this.auth.setSession(res);
        this.router.navigateByUrl('/dashboard');
      },
      error: (e) => alert(e?.error?.message ?? 'Erreur lors de la connexion'),
    });
  }
}
