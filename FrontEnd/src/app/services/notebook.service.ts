import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Notebook } from '../models/notebook';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class NotebookService {

  private apiUrl = `${environment.apiUrl}/api/notebooks`;

  constructor(private http: HttpClient) { }

  // Get all notebooks
  getNotebooks(): Observable<Notebook[]> {
    return this.http.get<Notebook[]>(this.apiUrl);
  }

  // Get a single notebook by id
  getNotebookById(id: string): Observable<Notebook> {
    return this.http.get<Notebook>(`${this.apiUrl}/${id}`);
  }

  getNotebookBystudentId(studentId: string): Observable<Notebook> {
    return this.http.get<Notebook>(`${this.apiUrl}/getnotebookbyStudentId/${studentId}`);
  }
  // Create a new notebook
  addNotebook(notebook: Notebook): Observable<Notebook> {
    return this.http.post<Notebook>(this.apiUrl, notebook);
  }

  // Update an existing notebook
  updateNotebook(id: string, notebook: Notebook): Observable<Notebook> {
    return this.http.put<Notebook>(`${this.apiUrl}/${id}`, notebook);
  }

  // Delete a notebook
  deleteNotebook(id: string): Observable<{}> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
