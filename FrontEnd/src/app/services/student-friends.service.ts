import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { StudentFriends } from '../models/StudentFriends';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class StudentFriendsService {
  private apiUrl = `${environment.apiUrl}/api/studentfriends`;

  constructor(private http: HttpClient) { }

  addFriendship(request: any): Observable<StudentFriends> {
    return this.http.post<StudentFriends>(this.apiUrl, request);
  }

  getFriendsByStudentId(studentId: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/${studentId}/friends`);
  }
  addFriend(friendEmail: string, courses: any[]): Observable<any> {
    return this.http.post('/sendFriendRequest', { friendEmail, courses });
  }
  sendFriendRequest(friend : any) : Observable<any> {
    return this.http.post('/sendFriendRequest', { friend });
  }
  
}