import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { UserDTO } from '../dto/user.dto';
import { API_URL } from '../../config';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${API_URL}/me`;

  private user = new BehaviorSubject<UserDTO | null>(null);
  currentUser = this.user.asObservable();

  constructor(private http: HttpClient) { }

  // Method to get user profile
  getUser(): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${API_URL}/me`, { withCredentials: true })
      .pipe(
        map(user => {
          user.password = undefined; // Limpiamos aquí también
          return user;
        }),
        tap(user => this.user.next(user)),
        catchError(error => {
          console.error('Error in getUser:', error);
          throw error;
        })
      );
  }

  // Method to update user profile
  updateUser(userProfile: UserDTO): Observable<UserDTO> {
    return this.http.put<UserDTO>(`${this.apiUrl}`, userProfile,{withCredentials:true}).pipe(
      catchError(err => {
        console.error('Error occurred while updating user profile:', err);
        return throwError(err);
      })
    );
  }
}
