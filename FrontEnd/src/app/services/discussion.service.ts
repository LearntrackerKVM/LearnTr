import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DiscussionService {
  private apiUrl = `${environment.apiUrl}/api/discussions`; // Adjust this to your actual backend URL

  constructor(private http: HttpClient) { }

  postDiscussion(discussion: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/save`, discussion);
  }

  replyToDiscussion(parentID: string, reply: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/reply/${parentID}`, reply);
  }
  
  likePost(discussionID: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/like/${discussionID}`, {});
  }

  dislikePost(discussionID: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/dislike/${discussionID}`, {});
  }

  getAllDiscussionsByCourseID(courseID: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/course/${courseID}`);
  }
}
