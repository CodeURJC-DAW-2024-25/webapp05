import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { TrainingCommentDTO } from '../dto/training-comment.dto';


@Injectable({
  providedIn: 'root',
})
export class TrainingCommentService {
  private readonly BASE_URL = '/api/trainingComments';

  constructor(private http: HttpClient) {}

  getTrainingComments(trainingId: number, page: number): Observable<any> {
    return this.http.get<TrainingCommentDTO[]>(`${this.BASE_URL}/?page=${page}&trainingId=${trainingId}`);
  }

  deleteTrainingComment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.BASE_URL}/${id}`);
  }

}