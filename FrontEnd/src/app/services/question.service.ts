import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class QuestionService {
  private baseUrl = `${environment.apiUrl}/api/questions`;

  constructor(private http: HttpClient) { }

  postQuestion(question: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/ask`, question);
  }
  postAnswer(questionId: string, answer: string): Observable<any> {
    const headers = new HttpHeaders().set('Content-Type', 'text/plain; charset=utf-8');
    return this.http.post(`${this.baseUrl}/answer/${questionId}`, answer, { headers, responseType: 'json' });
  }
  

  getAllQuestionsByStudentId(studentId: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/student/${studentId}`);
  }

  getAllQuestionsByProfessorId(professorId: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/professor/${professorId}`);
  }
  getAllQuestionsByCourseId(courseId: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/course/${courseId}`);
  }
}
 


