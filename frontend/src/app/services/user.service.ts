import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { UserDTO } from '../dto/user.dto';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl =  `/api/users`;

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
    return this.http.put<UserDTO>(`${this.apiUrl}/${userData.id}`, userData);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  unsubscribeFromTraining(id: number): Observable<any> {
    return this.http.post(`api/trainings/unsubscribed/${id}`, {}, { withCredentials: true , responseType: 'text'});
  }

  unsubscribeFromNutrition(id: number): Observable<any> {
    return this.http.post(`api/nutritions/unsubscribed/${id}`, {}, { withCredentials: true , responseType: 'text'});
  }

  reportedComments(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/reportedCommentsValues`);
  }
}
