import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, throwError } from 'rxjs';
import { environment } from '../environments/environment';
import { Assignment } from '../models/Asssignment';

@Injectable({
  providedIn: 'root'
})
export class AssignmentService {
  
  private apiUrl = `${environment.apiUrl}/api/assignments`;


  constructor(private http: HttpClient) { }


  createAssignment(assignment: Assignment): Observable<Assignment> {
    // Ensure that the dueDate property is a valid Date object
    if (!(assignment.dueDate instanceof Date)) {
      // Convert the dueDate string to a Date object
      assignment.dueDate = new Date(assignment.dueDate);
    }
    
    // Check if the dueDate property is a valid Date object
    if (isNaN(assignment.dueDate.getTime())) {
      // Handle invalid dueDate
      console.error('Invalid dueDate:', assignment.dueDate);
      return throwError('Invalid dueDate');
    }
  
    const formData: FormData = new FormData();
    formData.append('assignmentName', assignment.assignmentName);
    formData.append('assignmentNumber', assignment.assignmentNumber);
    formData.append('courseId', assignment.courseId);
    formData.append('courseName', assignment.courseName);
    formData.append('dueDate', assignment.dueDate.toISOString());
    formData.append('file', assignment.file, assignment.file.name);
      formData.append('createdDate', assignment.createdDate.toISOString());
  formData.append('createdById', assignment.createdById);
  formData.append('createdBy', assignment.createdBy);
  
    return this.http.post<Assignment>(this.apiUrl, formData);
  }
  

  getAssignments(): Observable<Assignment[]> {
    return this.http.get<Assignment[]>(this.apiUrl);
  }
  getAssignmentsByCourseId(courseId: string): Observable<Assignment[]> {
    return this.http.get<Assignment[]>(`${this.apiUrl}/getAssignments/${courseId}`);
  }
  getAssignmentQuestions(file : File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file,file.name);

    // Set headers if necessary, e.g., for file upload
    const headers = new HttpHeaders();
    headers.append('Content-Type', 'multipart/form-data');

    return this.http.post<any>(`${this.apiUrl}/questions`, formData, { headers });
  }

  getNoOfAssignmentsAddedByProfessor(createdById: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/getNoOfAssignments/${createdById}`);
  }
  downloadAssignmentFile( courseId: string, title: string): Observable<Blob> {
    const encodedTitle = encodeURIComponent(title); // Ensure the title is URL-safe
    const url = `${this.apiUrl}/file/${courseId}/${encodedTitle}`;
  
    // Note: 'observe' and 'responseType' options are crucial for handling blobs
    return this.http.get(url, { responseType: 'blob', observe: 'response' })
      .pipe(
        catchError(error => {
          console.error('Error downloading file:', error);
          return throwError(() => new Error('Error downloading file'));
        }),
        // Extract the blob from the HTTP response
        map(response => response.body as Blob)
      );
  }
  
}