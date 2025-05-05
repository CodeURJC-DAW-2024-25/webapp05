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
export class RegisterComponent implements OnInit {
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
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
    });
  }

  ngOnInit(): void {
    this.registerForm.valueChanges.subscribe(() => {
      this.registerErrorMessage = null;
    });
  }

  onRegister(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.isRegistering = true;
    this.registerErrorMessage = null;

    const credentials: RegisterRequest = {
      name: this.registerForm.value.username,
      email: this.registerForm.value.email,
      password: this.registerForm.value.password
    };

    const userRegistration: Omit<UserDTO, 'id'> = {
      name: credentials.name,
      email: credentials.email,
      password: credentials.password,
    };

    this.userService.registerUser(userRegistration).subscribe({
      next: () => {
        this.isRegistering = false;
        this.registerSuccess = true;
        this.registerForm.reset();
        this.router.navigate(['/login']);

        setTimeout(() => {
          this.registerSuccess = false;
        }, 3000);
      },
      error: (error) => {
        this.isRegistering = false;
        this.registerErrorMessage = error.message || 'Registration failed. Please try again.';
        this.registerForm.reset();
      },
    });
  }

  get regUsername() {
    return this.registerForm.get('name');
  }
  get regEmail() {
    return this.registerForm.get('email');
  }
  get regPassword() {
    return this.registerForm.get('password');
  }

}
