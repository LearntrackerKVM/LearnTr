import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, of, tap, throwError } from 'rxjs';
import { environment } from '../environments/environment';
import { FriendRequest } from '../models/FriendRequest';

@Injectable({
  providedIn: 'root'
})
export class FriendRequestService {


  private apiUrl = `${environment.apiUrl}/api/friendRequest`;
  constructor(private http: HttpClient) { }

  sendFriendRequest(friendRequest: FriendRequest): Observable<any> {
    return this.http.post(this.apiUrl + '/send', friendRequest);
  }

  getFriendRequests(userId: string): Observable<FriendRequest[]> {
    return this.http.get<FriendRequest[]>(`${this.apiUrl}/${userId}/friendRequests`);
  }
  acceptFriendRequest(requestId: string): Observable<boolean> {
    return this.http.post(`${this.apiUrl}/${requestId}/accept`, {}).pipe(
      map(() => true), // Map the response to true if successful
      catchError((error: HttpErrorResponse) => {
        console.error('Error accepting friend request:', error);
        return of(false); // Return false if there's an error
      })
    );
  }
  
  
  rejectFriendRequest(requestId: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/${requestId}/reject`, {}).pipe(
      map(() => true), // Map the response to true if successful
      catchError((error: HttpErrorResponse) => {
        console.error('Error accepting friend request:', error);
        return of(false); // Return false if there's an error
      })
    );
  }
  }