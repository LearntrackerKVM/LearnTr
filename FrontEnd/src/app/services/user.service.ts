import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
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
}
