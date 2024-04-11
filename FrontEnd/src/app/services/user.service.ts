import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { User } from './../models/user';
import { environment } from './../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/api/users`;
  private readonly TOKEN_KEY = 'currentUser';
  private loggedIn = new BehaviorSubject<boolean>(this.hasToken());
  private currentUserSubject = new BehaviorSubject<User | null>(this.getCurrentUserFromLocalStorage());

  constructor(private http: HttpClient) { }

  public currentUser$(): Observable<User | null> {
    return this.currentUserSubject.asObservable();
  }


  private getCurrentUserFromLocalStorage(): User | null {
    const userString = sessionStorage.getItem(this.TOKEN_KEY);
    return userString ? JSON.parse(userString) : null;
  }
  
  private hasToken(): boolean {
    return !!sessionStorage.getItem(this.TOKEN_KEY);
  }

  public isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  public setisLoggedIn(loggedIn: boolean, user?: User): void {
    debugger
    this.loggedIn.next(loggedIn);
    if (loggedIn && user) {
      sessionStorage.setItem(this.TOKEN_KEY, JSON.stringify(user));
    } else {
      sessionStorage.removeItem(this.TOKEN_KEY);
    }
  }

  public registerUser(user: User): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/register`, user);
  }

  fetchAllStudents(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/allstudents`);
  }
  public login(email: string, password: string): Observable<User> {
    return this.http.post<{message: string, user: User}>(`${this.apiUrl}/login`, { email, password })
      .pipe(
        map(response => {
          const user = response.user;
          if (user) {
            sessionStorage.setItem(this.TOKEN_KEY, JSON.stringify(user));
            this.currentUserSubject.next(user);
          }
          return user;
        })
      );
  }
  public logout(): void {
    sessionStorage.removeItem(this.TOKEN_KEY);
    this.currentUserSubject.next(null);
    this.loggedIn.next(false);
  }

  public getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  public updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/${user.id}`, user)
      .pipe(
        catchError(error => {
          // Handle or log error
          console.error('Error updating user', error);
          throw 'Error updating user: ' + error.message;
        })
      );
  }
  // In UserService
  public getUserByEmail(email: string): Observable<User> {
    // Encode the email to ensure special characters are correctly handled
    const encodedEmail = encodeURIComponent(email);
    // Use a query parameter for the email
    return this.http.get<User>(`${this.apiUrl}/byEmail?email=${encodedEmail}`)
      .pipe(
        catchError(error => {
          console.error('Error fetching user by email', error);
          // It's better to return an observable error to be handled by the subscriber
          return throwError(error);
        })
      );
  }

  public sendResetPasswordLink(email: string): Observable<string> {
    const headers = new HttpHeaders({'Content-Type': 'application/json'});
    return this.http.post<string>(
      `${this.apiUrl}/passwordresetrequest`,
      JSON.stringify({ email: email }), // Wrap the email in an object
      { headers: headers }
    );
  }
  resetPassword(token: string, newPassword: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });
    return this.http.post(`${this.apiUrl}/reset-password`, { token, newPassword }, { headers });
  }

  uploadProfilePicture(userId: string, file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file, file.name); // Match the backend expectation
    formData.append('userId', userId); // Include the userId as part of the FormData

    // Use the correct endpoint as per your backend configuration
    return this.http.post(`${this.apiUrl}/uploadProfilePicture`, formData, {
      reportProgress: true,
      observe: 'events'
    }).pipe(
      catchError(this.handleError)
    );
}


  private handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Backend returned code ${error.status}, ` +
        `body was: ${error.error}`);
    }
    // Return an observable with a user-facing error message.
    return throwError(
      'Something bad happened; please try again later.');
  }
}
