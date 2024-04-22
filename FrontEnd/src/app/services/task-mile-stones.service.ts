import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TaskMileStonesService {
  private apiUrl = `${environment.apiUrl}/api/tasksMilestones`;


  constructor(private http: HttpClient) { }

  getCompletedMilestonesByStudentid(studentId: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/getNoOfMilestones/${studentId}`);
  }
  getMilestonesByStudentTaskId(studentTaskId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/getmilestones/${studentTaskId}`);
  }
  updateMilestone(milestoneId: string, updatedMilestone: any): Observable<any> {
    const url = `${this.apiUrl}/updateMilestone/${milestoneId}`;
    return this.http.put(url, updatedMilestone);
  }
  getMilestonesWithUserInfo(studentTaskId: string, title: string): Observable<any> {
    const url = `${this.apiUrl}/getMilestonesWithUserInfo/${studentTaskId}/${title}`;
    return this.http.get(url);
  }
}