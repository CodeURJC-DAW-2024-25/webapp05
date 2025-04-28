import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LoginService } from '../../../services/login.service';
import { Router } from '@angular/router';
import { LoginRequest } from './../../../dto/user.dto';
import { UserService } from './../../../services/user.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LoginComponent {
  loginForm!: FormGroup;
  registerForm!: FormGroup;
  isLoading = false;
  errorMessage: string | null = null;
  isRegistering = false;
  registerSuccess = false;
  registerErrorMessage: string | null = null;

  constructor(private loginService: LoginService, private userService: UserService, private router: Router) { }

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
        this.isLoading = false;
        this.router.navigate(['/']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = err.message || 'Login failed. Please try again.';
        this.loginForm.get('password')?.reset();
      }
    });
  }

}
