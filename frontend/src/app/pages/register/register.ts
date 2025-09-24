import { Component, inject } from '@angular/core';
import { RegisterForm } from './components/register-form/register-form';
import { HttpClientModule } from '@angular/common/http';
import { AuthService } from '../../shared/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register-page',
  standalone: true,
  imports: [HttpClientModule, RegisterForm],
  templateUrl: './register.html',
})
export class Register {
  private auth = inject(AuthService);
  private router = inject(Router);

  onRegister(payload: { username: string; email: string; password: string }) {
    this.auth.register(payload.email, payload.username, payload.password).subscribe({
      next: (res) => {
        this.auth.setSession(res);
        this.router.navigateByUrl('/dashboard');
      },
      error: (e) => alert(e?.error?.message ?? "Erreur lors de l'inscription"),
    });
  }
}

