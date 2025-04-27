import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, of, throwError } from 'rxjs';
import { catchError, map, switchMap, tap } from 'rxjs/operators';
import { UserDTO } from '../dto/user.dto';
import { API_URL } from "../../config";

@Injectable({ providedIn: 'root' })
export class LoginService {
  public logged = new BehaviorSubject<boolean>(false);
  public user = new BehaviorSubject<UserDTO | null>(null);
  public token: string | null = localStorage.getItem('token');

  get isLogged() {
    return this.logged.asObservable();
  }

  get currentUser() {
    return this.user.asObservable();
  }

  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string): Observable<boolean> {
    console.log('Login attempt with username:', username);
    return this.http.post<any>(`${API_URL}/auth/login`, { username, password }, { observe: 'response', withCredentials: true })
      .pipe(
        switchMap(response => {
          if (response.status === 200) {
            this.logged.next(true);
            this.token = response.body?.token; //save token
            localStorage.setItem('token', this.token ?? '');  // save token in local storage
            console.log('Login successful, fetching user...');
            return this.fetchCurrentUser().pipe(
              tap(user => {
                if (user) {
                  console.log('User fetched after login:', user);
                  this.isAdmin().subscribe(isAdmin => console.log('Is Admin:', isAdmin));
                }
              }),
              map(() => true)
            );
          }
          return of(false);
        }),
        catchError(error => {
          console.error('Login error:', error);
          if (error.status === 401) {
            return of(false); // Incorrect password
          } else if (error.status === 404) {
            return throwError(() => new Error('User not found'));
          } else {
            return throwError(() => new Error('Unexpected error'));
          }
        })
      );
  }

  logout(): Observable<HttpResponse<any>> {
    console.log('Logging out...');
    return this.http.post<any>(`${API_URL}/auth/logout`, {}, { observe: 'response', withCredentials: true })
      .pipe(
        tap(response => {
          if (response.status === 200) {
            console.log('Logout successful');
            this.logged.next(false);
            this.user.next(null);
            this.token = null;
            localStorage.removeItem('token');  // remove token from local storage
            this.router.navigate(['/']);
          }
        })
      );
  }

  register(name: string, email: string, password: string, date: string): Observable<any> {
    console.log('Registering user with birth date:', date);
    return this.http.post(`${API_URL}/users/`, { name, email, password, date, roles: ["USER"] }, { observe: 'response', withCredentials: true })
      .pipe(
        tap(response => {
          if (response.status === 200) {
            this.logged.next(true);
            this.router.navigate(['/']);
          }
        })
      );
  }

  fetchCurrentUser(): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${API_URL}/me`, { withCredentials: true })
      .pipe(
        tap(user => {
          console.log('Fetched current user:', user);
          this.user.next(new UserDTO(
            user.id,
            user.name,
            user.roles ?? [],
            user.email,
            user.password
          ));
        }),
        catchError(error => {
          console.error('Error fetching current user:', error);
          throw error;
        })
      );
  }

  isAdmin(): Observable<boolean> {
    return this.currentUser.pipe(
      map(user => {
        if (user?.roles) {
          return user.roles.includes('ADMIN');
        }
        return false;
      })
    );
  }
}
