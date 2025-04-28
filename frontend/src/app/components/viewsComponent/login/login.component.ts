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
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private loginService: LoginService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  onLogin() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

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
