import { Component, EventEmitter, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { FloatLabel } from 'primeng/floatlabel';
import { InputText } from 'primeng/inputtext';

@Component({
  selector: 'app-login-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FloatLabel, InputText],
  templateUrl: './login-form.html',
})
export class LoginForm {
  private fb = inject(FormBuilder); // utiliser inject pour FormBuilder

  @Output() login = new EventEmitter<{ email: string; password: string }>(); // Événement émis lors de la soumission du formulaire

  // Définition du formulaire avec validation
  form = this.fb.group({
    // Validation des champs du formulaire
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required]],
  });

  // Méthode appelée lors de la soumission du formulaire
  submit() {
    if (this.form.invalid) return;
    const { email, password } = this.form.value as any;
    this.login.emit({ email, password });
  }
}

