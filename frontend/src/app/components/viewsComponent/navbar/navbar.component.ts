import { Component, OnInit } from '@angular/core';
import { LoginService } from '../../../services/login.service';
import { Router } from '@angular/router';
import { UserDTO } from '../../../dto/user.dto';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
})
export class NavbarComponent implements OnInit {
  isLogged = this.loginService.isLogged;
  isAdmin = this.loginService.isAdmin;
  user: UserDTO | null = null;

  constructor(
    public loginService: LoginService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loginService.currentUser.subscribe((user) => {
      this.user = user;
    });
  }

  logout(): void {
    this.loginService.logout().subscribe(() => {
      this.router.navigate(['/login']);
    });
  }

}

