import { Component, inject } from '@angular/core';
import { RegisterForm } from './components/register-form/register-form';
import UserService from '../../shared/services/user.service';
import { CreateUser }        from '../../shared/models/user.model';

@Component({
  selector: 'app-register-page',
  standalone: true,
  imports: [RegisterForm],
  templateUrl: './register.html',
})
export class Register {

  private userService = inject(UserService);

  onRegister(payload: CreateUser) {
    this.userService.register(payload).subscribe({
      next: (res) => {
        alert("Bienvenue " + JSON.stringify(res));
      },
      error: (err) => {
        alert(err?.error?.message || 'Erreur lors de l\'inscription');
      }
    })
  }
}
