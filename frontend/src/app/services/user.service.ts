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
  private apiUrl =  `/api/v1/users`;

  constructor(private http: HttpClient) { }

  getAllUsers(query?: string): Observable<UserDTO[]> {
    return this.http.get<UserDTO[]>(this.apiUrl, {
      params: query ? { query } : {}
    });
  }

  getUserById(id: number): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.apiUrl}/${id}`);
  }

  getCurrentUser(): Observable<UserDTO> {
    return this.http.get<UserDTO>(`${this.apiUrl}/me`);
  }

  registerUser(userData: Omit<UserDTO, 'id'>): Observable<{id: number}> {
    return this.http.post<{id: number}>(`${this.apiUrl}/new`, userData);
  }

  updateUser(userData: Partial<UserDTO>): Observable<UserDTO> {
    return this.http.put<UserDTO>(`${this.apiUrl}/me`, userData);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

}
