import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from '../../../services/login.service';
import { RegisterRequest } from '../../../dto/user.dto';
import { UserService } from '../../../services/user.service';
import { UserDTO } from '../../../dto/user.dto';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
})
export class RegisterComponent {
  registerForm!: FormGroup;
  registerErrorMessage: string | null = null;
  isRegistering = false;
  registerSuccess = false;

  constructor(
    private fb: FormBuilder,
    private loginService: LoginService,
    public router: Router,
    private userService: UserService
  ) {

    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(20)]],
    });
  }

  onRegister(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.isRegistering = true;
    this.registerErrorMessage = null;

    const registerData: RegisterRequest = {
      username: this.registerForm.value,
      email: this.registerForm.value,
      password: this.registerForm.value
    };

    const userRegistration: Omit<UserDTO, 'id'> = {
      username: registerData.username,
      email: registerData.email,
      password: registerData.password,
    };

    this.userService.registerUser(userRegistration).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (error) => {
        this.registerErrorMessage = error.message || 'Registration failed. Please try again.';
      },
    });
  }

  get regUsername() {
    return this.registerForm.get('username');
  }
  get regRmail() {
    return this.registerForm.get('email');
  }
  get regPassword() {
    return this.registerForm.get('password');
  }

}
