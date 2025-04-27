import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Video } from '../models/video';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class VideoService {
  private apiUrl = environment.apiUrl + '/api/videos';

  constructor(private http: HttpClient) { }

  getAllVideos(): Observable<Video[]> {
    return this.http.get<Video[]>(this.apiUrl);
  }

  getLatestVideos(): Observable<Video[]> {
    return this.http.get<Video[]>(`${this.apiUrl}/latest`);
  }

  getTrendingVideos(): Observable<Video[]> {
    return this.http.get<Video[]>(`${this.apiUrl}/trending`);
  }

  searchVideos(query: string): Observable<Video[]> {
    return this.http.get<Video[]>(`${this.apiUrl}/search?query=${query}`);
  }

  getVideoById(id: number): Observable<Video> {
    return this.http.get<Video>(`${this.apiUrl}/${id}`);
  }

  getVideosByUserId(userId: number): Observable<Video[]> {
    return this.http.get<Video[]>(`${this.apiUrl}/user/${userId}`);
  }

  createVideo(video: Video, userId: number): Observable<Video> {
    return this.http.post<Video>(`${this.apiUrl}/user/${userId}`, video);
  }

  updateVideo(id: number, video: Video): Observable<Video> {
    return this.http.put<Video>(`${this.apiUrl}/${id}`, video);
  }

  deleteVideo(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  incrementViews(id: number): Observable<Video> {
    return this.http.post<Video>(`${this.apiUrl}/${id}/view`, {});
  }

  likeVideo(id: number): Observable<Video> {
    return this.http.post<Video>(`${this.apiUrl}/${id}/like`, {});
  }

  dislikeVideo(id: number): Observable<Video> {
    return this.http.post<Video>(`${this.apiUrl}/${id}/dislike`, {});
  }

  uploadVideo(file: File): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<string>(`${this.apiUrl}/upload`, formData);
  }

  uploadThumbnail(file: File): Observable<string> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<string>(`${this.apiUrl}/thumbnail/upload`, formData);
  }
}
