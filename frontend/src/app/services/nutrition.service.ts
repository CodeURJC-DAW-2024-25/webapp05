import { Injectable } from '@angular/core';
import { Nutrition } from '../dto/nutrition.dto';
import { Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';


@Injectable({
  providedIn: 'root',
})
export class NutritionService {

  private readonly baseUrl = "/api/nutritions/";

  constructor(private http: HttpClient) {}

  getNutritionById(id: number): Observable<any> {
    return this.http.get<Nutrition>(`${this.baseUrl}${id}`);
  }

  subscribeToNutrition(id: number): Observable<any> {
    return this.http.post(`${this.baseUrl}subscribed/${id}`,{}, { withCredentials: true , responseType: 'text'});
  }

  unsubscribeFromNutrition(id: number): Observable<any> {
    return this.http.post(`${this.baseUrl}unsubscribed/${id}`, {}, { withCredentials: true , responseType: 'text'});
  }

  isSubscribed(id: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}isSubscribed/${id}`);
  }

  deleteNutrition(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`, { withCredentials: true });
  }

  updateNutrition(id: number, nutrition: Nutrition): Observable<any> {
    return this.http.put<Nutrition>(`${this.baseUrl}/${id}`, nutrition);
  }

  createNutrition(nutrition: Nutrition): Observable<Nutrition> {
    return this.http.post<Nutrition>(`${this.baseUrl}`, nutrition, { withCredentials: true });
  }

  getNutritions(page: number): Observable<Nutrition[]> {
    return this.http.get<Nutrition[]>(`${this.baseUrl}paginated?page=${page}`);
  }

  uploadNutritionImage(id: number, imageFile: File) {
    const formData = new FormData();
    formData.append('imageFile', imageFile);

    return this.http.put<any>(`${this.baseUrl}${id}/image`, formData);
  }
}
