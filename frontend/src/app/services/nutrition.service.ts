import { Injectable } from '@angular/core';
import { Nutrition } from '../dto/nutrition.dto';
import { Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';


@Injectable({
  providedIn: 'root',
})
export class NutritionService {
  private nutritions: Nutrition[] = [
    {
      id: 1,
      name: 'Nutricion de deficit calorico',
      calories: 200,
      goal:'Lose weight',
      description: 'Nutricion basada en la ingesta de proteinas',
    },
    {
      id: 2,
      name: 'Nutricion de superavit calorico',
      calories: 400,
      goal:'Increase weight',
      description: 'Nutriicion basada en la ingesta de carbohidratos',
    },
  ];

  private baseUrl = "/api/nutritions/";

  constructor(private http: HttpClient) {}

  getNutritionById(id: number): Observable<Nutrition> {
    return this.http.get<Nutrition>(`${this.baseUrl}/${id}`);
  }

  subscribeToNutrition(id: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/${id}/subscribe`, {}, { withCredentials: true });
  }

  unsubscribeFromNutrition(id: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/${id}/unsubscribe`, {}, { withCredentials: true });
  }

  isSubscribed(id: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/${id}/isSubscribed`, { withCredentials: true });
  }

  deleteNutrition(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { withCredentials: true });
  }

  updateNutrition(id: number, nutrition: Nutrition): Observable<Nutrition> {
    return this.http.put<Nutrition>(`${this.baseUrl}/${id}`, nutrition, { withCredentials: true });
  }

  createNutrition(nutrition: Nutrition): Observable<Nutrition> {
    return this.http.post<Nutrition>(`${this.baseUrl}`, nutrition, { withCredentials: true });
  }

  getAllNutritions(): Observable<Nutrition[]> {
    return this.http.get<Nutrition[]>(`${this.baseUrl}`);
  }

  /*getTrainingsPaginated(page: number, size: number): Observable<Training[]> {
    return this.http.get<Training[]>(`${this.baseUrl}?page=${page}&size=${size}`);
  }*/
}
