import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { Course } from '../models/Course';
import { CourseDetails } from '../models/CourseDetails';
import {  CourseSchedule } from '../models/CourseSchedule';
import { User } from '../models/user';

@Injectable({
  providedIn: 'root'
})
export class CourseService {

  private apiUrl = `${environment.apiUrl}/api/courses`;
  private readonly TOKEN_KEY = 'currentUser';

  constructor(private http:HttpClient) { }

  public createNewCourse(course : Course ): Observable<Course>{
    return this.http.post<Course>(`${this.apiUrl}/RegisterCourse`,course)
  } 

  public addCourseScheudle(courseSchedule : CourseSchedule[] ): Observable<CourseSchedule[]>{
    return this.http.post<CourseSchedule[]>(`${this.apiUrl}/AddCourseschedule`,courseSchedule)
  } 

  getCoursesByprofessorId(professorId: number): Observable<Course[]> {
    const url = `${this.apiUrl}/getCoursesbyId/${professorId}`;
    return this.http.get<Course[]>(url);
  }
  getCoursesSchedulesByCourseIds(courseIds: string[]): Observable<CourseSchedule[]> {
    const params = courseIds.join(',');
    const url = `${this.apiUrl}/getSchedulebyCourses?courseIds=${params}`;
    return this.http.get<CourseSchedule[]>(url);
  }
  
  getAllCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(this.apiUrl+ '/getallCourses');
  }

  addCourseForStudent(request : any): Observable<CourseDetails> {
    return this.http.post<CourseDetails>(`${this.apiUrl}/addcourseforstudent`, request);
  }

  getCoursesForStudent(studentId: string): Observable<CourseDetails[]> {
    return this.http.get<CourseDetails[]>(`${this.apiUrl}/courses?studentId=${studentId}`);
  }

  getNoOfCoursesAddedByProfessor(createdById: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/getNoOfCourses/${createdById}`);
  }

  getNumberOfStudentsEnrolled(createdById: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/getNoOfStudentsEnrolled/${createdById}`);
  }

  getNumberOfCoursesEnrolledbyStudent(studentId: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/getNoOfCoursesEnrolledbyStudent/${studentId}`);
  }

  getAllStudentsEnrolledInCoursesAddedByProfessor(professorId: string): Observable<User[]> {
    const url = `${this.apiUrl}/enrolled/${professorId}`;
    return this.http.get<User[]>(url);
  }
  getCoursesWithTasksAndMilestonesByStudentId(courseId: string, studentId: string): Observable<any> {
    // Ensure both courseId and studentId are appended as query parameters to the request URL
    const url = `${this.apiUrl}/withTasksandMilestones?courseId=${courseId}&studentId=${studentId}`;
    return this.http.get(url);
  }
}

