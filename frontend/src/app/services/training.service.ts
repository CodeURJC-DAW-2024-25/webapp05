import { Injectable } from '@angular/core';
import { Training } from '../dto/training.dto';
import { Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';


@Injectable({
  providedIn: 'root',
})
export class TrainingService {

  private readonly baseUrl = "/api/trainings/";

  constructor(private http: HttpClient) {}

  getTrainingById(id: number): Observable<any> {
    return this.http.get<Training>(`${this.baseUrl}${id}`);
  }

  subscribeToTraining(id: number): Observable<any> {
        return this.http.post(`${this.baseUrl}subscribed/${id}`,{}, { withCredentials: true , responseType: 'text'});
      }

  unsubscribeFromTraining(id: number): Observable<any> {
    return this.http.post(`${this.baseUrl}unsubscribed/${id}`, {}, { withCredentials: true , responseType: 'text'});
  }

  isSubscribed(id: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}isSubscribed/${id}`);
  }

  deleteTraining(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}${id}`, { withCredentials: true });
  }

  updateTraining(id: number, training: Training): Observable<any> {
    return this.http.put<Training>(`${this.baseUrl}${id}`, training, { withCredentials: true} );
  }

  createTraining(training: Training): Observable<any> {
    return this.http.post<Training>(`${this.baseUrl}`, training, { withCredentials: true});
  }

  getTrainings(page: number): Observable<Training[]> {
    return this.http.get<Training[]>(`${this.baseUrl}paginated?page=${page}`);
  }

  uploadTrainingImage(id: number, imageFile: File) {
    const formData = new FormData();
    formData.append('imageFile', imageFile);

    return this.http.put<any>(`${this.baseUrl}${id}/image`, formData);
  }
}
