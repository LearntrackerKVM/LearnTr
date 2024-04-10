import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user'; // Assuming you have a User model defined
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class LeaderBoardService {

  private baseUrl = `${environment.apiUrl}/api/leaderboard`; // Base URL of your backend API

  constructor(private http: HttpClient) { }

  getAllStudentsByRanking(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/students/rank`);
  }

  getStudentFriends(userId: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/friends/${userId}`);
  }

  getStudentsWithSameCourses(userId: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/courses/same/${userId}`);
  }

  getFriendsWithSameCourses(userId: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/friends/courses/same/${userId}`);
  }
}
