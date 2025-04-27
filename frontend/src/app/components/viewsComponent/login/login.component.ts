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
    this.loginService.logIn(this.username, this.password).subscribe(
      (_) => {
        console.log('Login successful');
        this.loginService.reqIsLogged(); // Actualizar estado
        this.router.navigate(['/']); // Redirigir a home (o donde quieras)
      },
      (error) => {
        console.error('Login failed', error);
        // Aquí podrías mostrar un error en pantalla si quieres
      }
    );
  }
}
