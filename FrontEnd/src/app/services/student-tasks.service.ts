import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class StudentTasksService {
  private apiUrl = `${environment.apiUrl}/api/studentTasks`;


  constructor(private http: HttpClient) { }
  
  getAllStudentTasks(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

    
  getAllStudentTasksByStudentId(studentId: any): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${studentId}`);
  }

  getAllTasksByStudentIdandCourseId(studentId : any,courseId : any): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${studentId}/${courseId}`);
  }
  // Fetch a single student task by ID
  getStudentTaskById(id: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  // Create a new student task
  createStudentTask(task: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, task);
  }

  // // Update an existing student task
  // updateStudentTask(id: string, task: any): Observable<any> {
  //   return this.http.put<any>(`${this.apiUrl}/${id}`, task);
  // }

  // Delete a student task
  deleteStudentTask(id: string): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`);
  }

  getNoOfExamsFroStudent(studentId: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/getNoOfExams/${studentId}`);
  }
  getNoOfAssignmentsAddedForStudent(studentId: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/getNoOfAssignments/${studentId}`);
  }
  updateStudentTask(id: string, difficultyLevel: string, notes: string, status: string, completionDate?: string): Observable<any> {

    let params = new HttpParams()
        .set('difficultyLevel', difficultyLevel)
        .set('notes', notes)
        .set('status', status);
    
    // Add completionDate if it is provided
    if (completionDate) {
        params = params.set('completionDate', completionDate);
    }

    return this.http.put<any>(`${this.apiUrl}/${id}`, {}, { params: params });
  }
  uploadFileToStudentTask(studentTaskId: string, file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);

    return this.http.post(`${this.apiUrl}/uploadFile/${studentTaskId}`, formData, {
      reportProgress: true,
      observe: 'events'
    }).pipe(
      catchError(this.handleError)
    );
  }

// Retrieve the file for a specific student task
getFile(studentTaskId: string): Observable<Blob> {
  return this.http.get(`${this.apiUrl}/file/${studentTaskId}`, { responseType: 'blob' }).pipe(
    catchError(this.handleError)
  );
}

// Error handling
private handleError(error: HttpErrorResponse) {
  if (error.error instanceof ErrorEvent) {
    // A client-side or network error occurred. Handle it accordingly.
    console.error('An error occurred:', error.error.message);
  } else {
    // The backend returned an unsuccessful response code.
    console.error(
      `Backend returned code ${error.status}, ` +
      `body was: ${error.error}`);
  }
  // return an observable with a user-facing error message
  return throwError(
    'Something bad happened; please try again later.');
}
}