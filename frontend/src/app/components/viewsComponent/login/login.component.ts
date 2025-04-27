import { Component } from '@angular/core';
import { LoginService } from '../../../services/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
})
export class LoginComponent {
  username: string = '';
  password: string = '';

  constructor(private loginService: LoginService, private router: Router) { }

  onSubmit() {
    this.loginService.isLogged.subscribe({
      next: (loggedStatus) => {
        // Check if the user is logged in
        if (loggedStatus) {
          // the user is logged in
        } else {
          // the user is not logged in
        }
      },
      error: (err) => {
        console.error('Error checking logged status', err);
      }
    });

  }

}
