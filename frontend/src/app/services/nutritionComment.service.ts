import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { NutritionCommentDTO } from '../dto/nutrition-comment.dto';

@Injectable({
    providedIn: 'root',
})
export class NutritionCommentService {
    private readonly BASE_URL = '/api/nutritionComments';

    constructor(private http: HttpClient) { }

    getNutritionComments(nutritionId: number, page: number): Observable<any> {
        return this.http.get<NutritionCommentDTO[]>(`${this.BASE_URL}/?page=${page}&nutritionId=${nutritionId}`);
    }

    createComment(comment: NutritionCommentDTO): Observable<any> {
        return this.http.post<NutritionCommentDTO>(`${this.BASE_URL}/`, comment);
    }

    reportNutritionComment(id: number): Observable<any> {
        return this.http.put<void>(`${this.BASE_URL}/report?commentId=${id}`, {});
    }

    unreportNutritionComment(id: number): Observable<any> {
        return this.http.put<void>(`${this.BASE_URL}/valid?commentId=${id}`, {});
    }

    deleteNutritionComment(id: number): Observable<void> {
        return this.http.delete<void>(`${this.BASE_URL}/${id}`);
    }

    getNutritionCommentById(id: number): Observable<any> {
        return this.http.get<NutritionCommentDTO>(`${this.BASE_URL}/comment/${id}/`);
    }

    updateNutritionComment(id: number, comment: NutritionCommentDTO): Observable<any> {
        return this.http.put<NutritionCommentDTO>(`${this.BASE_URL}/?id=${id}`, comment);
    }

}