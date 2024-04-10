import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { Exam } from '../models/Exam';

@Injectable({
  providedIn: 'root'
})
export class ExamService {


  private apiUrl = `${environment.apiUrl}/api/exams`;
  private readonly TOKEN_KEY = 'currentUser';

  constructor(private http: HttpClient) {

  }

  getAllExams(): Observable<Exam[]> {
    return this.http.get<Exam[]>(this.apiUrl);
  }

  getExamById(id: string): Observable<Exam> {
    return this.http.get<Exam>(`${this.apiUrl}/${id}`);
  }

  createExam(exam: Exam): Observable<Exam> {
    return this.http.post<Exam>(this.apiUrl, exam);
  }

  deleteExam(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
  getNoOfExamsAddedByProfessor(createdById: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/getNoOfExams/${createdById}`);
  }
}