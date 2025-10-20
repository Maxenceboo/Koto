import { Component, inject } from '@angular/core';
import { LoginForm } from './components/login-form/login-form';
import { LoginUser } from '../../shared/models/auth.model';
import AuthService from '../../shared/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [LoginForm],
  templateUrl: './login.html',
})
export class Login {

  private authService = inject(AuthService);

  private router = new Router();

  onLogin(payload: LoginUser) {
    this.authService.login(payload).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },
      error: err => {
        console.log(err);
      }
    })
  }
}
