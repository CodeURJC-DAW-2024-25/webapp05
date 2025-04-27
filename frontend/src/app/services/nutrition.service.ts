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
    return this.http.delete(`${this.baseUrl}/${id}`, { withCredentials: true });
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

  updateNutrition(id: number, nutrition: Nutrition): Observable<any> {
    return this.http.put<Nutrition>(`${this.baseUrl}/${id}`, nutrition);
  }

  createNutrition(nutrition: Nutrition): Observable<Nutrition> {
    return this.http.post<Nutrition>(`${this.baseUrl}`, nutrition, { withCredentials: true });
  }

  getNutritions(page: number): Observable<Nutrition[]> {
    return this.http.get<Nutrition[]>(`${this.baseUrl}paginated?page=${page}`);
  }

  /*getNutritionsPaginated(page: number, size: number): Observable<Nutrition[]> {
    return this.http.get<Nutrition[]>(`${this.baseUrl}?page=${page}&size=${size}`);
  }
  
   loadNutrition(nutritionId?: number): Observable<Nutrition | null> {
     if (nutritionId != null && !isNaN(nutritionId)) {
       return this.http.get<Nutrition>(`${this.baseUrl}/${nutritionId}`);
     } else {
       console.log('No hay nutritionId, no se carga ningún nutrition');
       return of(null); // Retorna un observable vacío
     }
   }
 
   // Save nutrition
   savenutrition(nutrition: Nutrition): Observable<Nutrition> {
     if (nutrition.id != null && !isNaN(nutrition.id)) {
       // Edit
       return this.http.put<Nutrition>(`${this.baseUrl}/${nutrition.id}`, nutrition);
     } else {
       // Create
       return this.http.post<Nutrition>(this.baseUrl, nutrition);
     }
   }  
  */
}
