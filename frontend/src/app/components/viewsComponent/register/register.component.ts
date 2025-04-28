import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from '../../../services/login.service';
import { RegisterRequest } from '../../../dto/user.dto';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  registerErrorMessage: string | null = null;
  isRegistering = false;
  registerSuccess = false;

  constructor(
    private fb: FormBuilder,
    private loginService: LoginService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  onRegister(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.registerErrorMessage = null;

    const credentials: RegisterRequest = {
      username: this.registerForm.value,
      email: this.registerForm.value,
      password: this.registerForm.value };

    this.loginService.register(credentials).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (error) => {
        this.registerErrorMessage = error.message || 'Registration failed. Please try again.';
      },
    });
  }
}
