import { Component, EventEmitter, Output, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  AbstractControl,
  FormBuilder,
  ReactiveFormsModule,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import {FloatLabel} from 'primeng/floatlabel';
import {InputText} from 'primeng/inputtext';

// Validation personnalisée pour vérifier que les mots de passe correspondent
const passwordsMatch: ValidatorFn = (ctrl: AbstractControl): ValidationErrors | null => {
  const p: string = ctrl.get('password')?.value;
  const c: string = ctrl.get('confirm')?.value;
  return p && c && p !== c ? { mismatch: true } : null;
};

@Component({
  selector: 'app-register-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FloatLabel, InputText],
  templateUrl: './register-form.html',
})
export class RegisterForm {
  private fb = inject(FormBuilder); // utiliser inject pour FormBuilder

  @Output() register = new EventEmitter<{ username: string; email: string; password: string }>(); // Événement émis lors de la soumission du formulaire

  // Définition du formulaire avec validation
  form = this.fb.group({
    // Validation des champs du formulaire
    username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
    confirm: ['', [Validators.required]],
  },
    // Validation personnalisee pour vérifier que les mots de passe correspondent
    { validators: passwordsMatch }
  );
  // Méthode appelee lors de la soumission du formulaire

  submit() {
    if (this.form.invalid) return;
    const { username, email, password } = this.form.value as any;
    this.register.emit({ username, email, password });
  }
}
