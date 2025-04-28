import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { catchError, map, switchMap, tap } from 'rxjs/operators';
import { LoginRequest, UserDTO, RegisterRequest } from '../dto/user.dto';
import { API_URL } from "../../config";

export interface LoginResponse {
  status: 'SUCCESS' | 'ERROR';
  message?: string;
  token?: string;
}

export interface RegisterResponse {
  name: string;
  email: string;
  password: string;
}


export interface DecodedToken {
  sub: string;
  roles: string[];
  exp: number;
}

@Injectable({ providedIn: 'root' })

export class LoginService {
  private readonly API_URL = `/api/v1/auth`;
  private logged = new BehaviorSubject<boolean>(false);
  private user = new BehaviorSubject<UserDTO | null>(null);
  private admin = new BehaviorSubject<boolean>(false);

  constructor(private http: HttpClient, private router: Router) {
    this.checkAuthStatus();
  }

  get isLogged() {
    return this.logged.asObservable();
  }

  get currentUser() {
    return this.user.asObservable();
  }

  get isAdmin() {
    return this.admin.asObservable();
  }

  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${API_URL}/auth/login`, credentials, {
      withCredentials: true
    }).pipe(
      tap(response => this.handleLoginSuccess(response)),
      catchError(error => this.handleAuthError(error))
    );
  }

  register(credentials: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${API_URL}/auth/register`, credentials, {
      withCredentials: true
    }).pipe(
      catchError(error => this.handleAuthError(error))
    );
  }

  logout(): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.API_URL}/logout`, {}, {
      withCredentials: true
    }).pipe(
      tap(() => this.handleLogoutSuccess()),
      catchError(error => this.handleAuthError(error))
    );
  }

  refreshToken(): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.API_URL}/refresh`, {}, {
      withCredentials: true
    }).pipe(
      tap(response => this.handleRefreshSuccess(response)),
      catchError(error => this.handleAuthError(error))
    );
  }

  private handleRefreshSuccess(response: LoginResponse): void {
    if (response.status === 'SUCCESS') {
      this.logged.next(true);
    }
  }

  private fetchUserInfo(): void {
    this.http.get<UserDTO>(`/api/v1/users/me`, { withCredentials: true })
      .subscribe({
        next: (user: UserDTO) => {
          this.user.next(user);
          const isAdmin = (user.roles ?? []).includes('ADMIN');
          this.admin.next(isAdmin);        },
        error: () => {
          this.user.next(null);
          this.admin.next(false);
        }
      });
  }

  private handleLoginSuccess(response: LoginResponse): void {
    if (response.status === 'SUCCESS') {
      this.logged.next(true);
      this.fetchUserInfo();
      this.router.navigate(['/']);
    }
  }

  private handleLogoutSuccess(): void {
    this.logged.next(false);
    this.router.navigate(['/login']);
  }

  private handleAuthError(error: any): Observable<never> {
    this.logged.next(false);
    const errorMessage = error.error?.message || 'Authentication failed';
    return throwError(() => new Error(errorMessage));
  }

  private checkAuthStatus(): void {
    this.refreshToken().subscribe({
      next: () => this.fetchUserInfo(),
      error: () => {  }
    });
  }

  get currentAuthStatus(): boolean {
    return this.logged.value;
  }
}
