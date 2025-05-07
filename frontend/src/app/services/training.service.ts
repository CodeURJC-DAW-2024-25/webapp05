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






  /*getTrainingsPaginated(page: number, size: number): Observable<Training[]> {
    return this.http.get<Training[]>(`${this.baseUrl}?page=${page}&size=${size}`);
  }*/

  /*
  loadTraining(trainingId?: number): Observable<Training | null> {
    if (trainingId != null && !isNaN(trainingId)) {
      return this.http.get<Training>(`${this.baseUrl}/${trainingId}`);
    } else {
      console.log('No hay trainingId, no se carga ningún training');
      return of(null); // Retorna un observable vacío
    }
  }

  // Save training
  saveTraining(training: Training): Observable<Training> {
    if (training.id != null && !isNaN(training.id)) {
      // Edit
      return this.http.put<Training>(`${this.baseUrl}/${training.id}`, training);
    } else {
      // Create
      return this.http.post<Training>(this.baseUrl, training);
    }
  }
    */
}
