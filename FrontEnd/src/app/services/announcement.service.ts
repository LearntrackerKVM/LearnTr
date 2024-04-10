import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { Announcement } from '../models/Announcement';

@Injectable({
  providedIn: 'root'
})
export class AnnouncementService {

  private apiUrl = `${environment.apiUrl}/api/announcements`;

  constructor(private http: HttpClient) {}

  // Method to create a new announcement
  createAnnouncement(announcement: Announcement): Observable<Announcement> {
    return this.http.post<Announcement>(this.apiUrl, announcement);
  }

  // Method to retrieve all announcements
  getAnnouncements(): Observable<Announcement[]> {
    return this.http.get<Announcement[]>(this.apiUrl);
  }

  // Method to retrieve a single announcement by ID
  getAnnouncementById(id: string): Observable<Announcement> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.get<Announcement>(url);
  }

  // Method to update an existing announcement
  updateAnnouncement(announcement: Announcement): Observable<Announcement> {
    const url = `${this.apiUrl}/${announcement.id}`;
    return this.http.put<Announcement>(url, announcement);
  }

  // Method to delete an announcement by ID
  deleteAnnouncement(id: string): Observable<void> {
    const url = `${this.apiUrl}/${id}`;
    return this.http.delete<void>(url);
  }


  getAnnouncementsByCourseId(courseId: string): Observable<Announcement[]> {
    const url = `${this.apiUrl}/byCourseId/${courseId}`;
    return this.http.get<Announcement[]>(url);
  }
}
