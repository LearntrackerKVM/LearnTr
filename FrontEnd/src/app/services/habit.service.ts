import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { Habit } from '../models/Habit';

@Injectable({
  providedIn: 'root'
})
export class HabitService {

  private apiUrl = `${environment.apiUrl}/api/habits`;

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };

  constructor(private http: HttpClient) { }

  // Get all habits
  getAllHabits(): Observable<Habit[]> {
    return this.http.get<Habit[]>(this.apiUrl, this.httpOptions);
  }

  // Get a single habit by ID
  getHabitById(id: string): Observable<Habit> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.get<Habit>(url, this.httpOptions);
  }

  // Create a new habit
  createHabit(habit: Habit): Observable<Habit> {
    return this.http.post<Habit>(this.apiUrl, habit, this.httpOptions);
  }

  // Update an existing habit
  updateHabit(id: string, habit: Habit): Observable<any> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.put(url, habit, this.httpOptions);
  }

  // Delete a habit
  deleteHabit(id: string): Observable<any> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.delete(url, this.httpOptions);
  }

  // Get habits by student ID
  getHabitsByStudentId(studentId: string): Observable<Habit[]> {
    const url = `${this.apiUrl}/student/${studentId}`;
    return this.http.get<Habit[]>(url, this.httpOptions);
  }
}