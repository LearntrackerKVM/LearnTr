import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';// Ensure you have this model
import { JournalEntry } from '../models/JouranlEntry';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class JournalService {
  private apiUrl = `${environment.apiUrl}/api/journal`;

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) {}

  // Fetch all journal entries by student ID
  getJournalEntriesByStudentId(studentId: string): Observable<JournalEntry[]> {
    return this.http.get<JournalEntry[]>(`${this.apiUrl}/${studentId}`);
  }

  // Fetch a single journal entry by ID
  getJournalEntryById(id: string): Observable<JournalEntry> {
    return this.http.get<JournalEntry>(`${this.apiUrl}/entry/${id}`);
  }

  // Create a new journal entry
  addJournalEntry(entry: JournalEntry): Observable<JournalEntry> {
return this.http.post<JournalEntry>(this.apiUrl, entry, this.httpOptions);
  }

  // Update an existing journal entry
  updateJournalEntry(id: string, entry: JournalEntry): Observable<JournalEntry> {
    return this.http.put<JournalEntry>(`${this.apiUrl}/${id}`, entry, this.httpOptions);
  }

  // Delete a journal entry
  deleteJournalEntry(id: string): Observable<{}> {
    return this.http.delete(`${this.apiUrl}/${id}`, this.httpOptions);
  }


  getJournalEntryByDateAndStudentId(date: Date, studentId: string): Observable<JournalEntry> {
    let date1 ='';
    if (typeof date !== 'string') {
      date1 = this.toISODateString(date);
    }
    else{
      date1 = date;
    }
    return this.http.get<JournalEntry>(`${this.apiUrl}/${studentId}/${date1}`);
  }

  private toISODateString(date: Date): string {
    return date.toISOString().split('T')[0]; // Format date as 'YYYY-MM-DD'
  }
}
