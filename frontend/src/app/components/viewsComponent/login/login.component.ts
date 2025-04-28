import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginService } from '../../../services/login.service';
import { Router } from '@angular/router';
import { LoginRequest } from './../../../dto/user.dto';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LoginComponent {
  loginForm!: FormGroup;
  errorMessage: string | null = null;
  isLoading = false;

  constructor(
    private fb: FormBuilder,
    private loginService: LoginService,
    private router: Router
  ){
    this.loginForm = this.fb.group({
      email: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });

  }

  onLogin() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = null;

    const credentials: LoginRequest = {
      email: this.loginForm.value,
      password: this.loginForm.value };


    this.loginService.login(credentials).subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: (error) => {
        this.errorMessage = error.message || 'Login failed. Please try again.';
        this.loginForm.get('password')?.reset();
      }
    });
  }

}
