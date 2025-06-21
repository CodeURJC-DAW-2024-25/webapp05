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

  createComment(comment: TrainingCommentDTO): Observable<any> {
      return this.http.post<TrainingCommentDTO>(`${this.BASE_URL}/`, comment);
  }

  reportTrainingComment(id: number): Observable<any> {
    return this.http.put<void>(`${this.BASE_URL}/report?commentId=${id}`, {});
  }

  unreportTrainingComment(id: number): Observable<any> {
    return this.http.put<void>(`${this.BASE_URL}/valid?commentId=${id}`, {});
  }

  deleteTrainingComment(id: number): Observable<any> {
    return this.http.delete<void>(`${this.BASE_URL}/?id=${id}`);
  }

  getTrainingCommentById(id: number): Observable<any> {
    return this.http.get<TrainingCommentDTO>(`${this.BASE_URL}/comment/${id}/`);
  }

  updateTrainingComment(id: number, comment: TrainingCommentDTO): Observable<any> {
    return this.http.put<TrainingCommentDTO>(`${this.BASE_URL}/?id=${id}`, comment);
  }

}