import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { UserDTO } from '../dto/user.dto';
import { API_URL } from '../../config';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${API_URL}/me`;

  constructor(private http: HttpClient) { }

  // Method to get user profile
  getUserProfile(): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.apiUrl}`,{withCredentials:true}).pipe(
      catchError(err => {
        console.error('Error occurred while fetching user profile:', err);
        return throwError(err);
      })
    );
  }

  // Method to update user profile
  updateUserProfile(userProfile: UserDTO): Observable<UserDTO> {
    return this.http.put<UserDTO>(`${this.apiUrl}`, userProfile,{withCredentials:true}).pipe(
      catchError(err => {
        console.error('Error occurred while updating user profile:', err);
        return throwError(err);
      })
    );
  }
}
