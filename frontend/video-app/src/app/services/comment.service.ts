import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment } from '../models/comment';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  private apiUrl = environment.apiUrl + '/api/comments';

  constructor(private http: HttpClient) { }

  getCommentsByVideoId(videoId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.apiUrl}/video/${videoId}`);
  }

  getCommentsByUserId(userId: number): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.apiUrl}/user/${userId}`);
  }

  getCommentById(id: number): Observable<Comment> {
    return this.http.get<Comment>(`${this.apiUrl}/${id}`);
  }

  createComment(comment: Comment, userId: number, videoId: number): Observable<Comment> {
    return this.http.post<Comment>(`${this.apiUrl}/video/${videoId}/user/${userId}`, comment);
  }

  updateComment(id: number, comment: Comment): Observable<Comment> {
    return this.http.put<Comment>(`${this.apiUrl}/${id}`, comment);
  }

  deleteComment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  likeComment(id: number): Observable<Comment> {
    return this.http.post<Comment>(`${this.apiUrl}/${id}/like`, {});
  }

  dislikeComment(id: number): Observable<Comment> {
    return this.http.post<Comment>(`${this.apiUrl}/${id}/dislike`, {});
  }
}
