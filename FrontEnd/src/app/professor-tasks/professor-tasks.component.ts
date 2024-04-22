import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Course } from '../models/Course';
import { AssignmentService } from '../services/assignment.service';
import { CourseService } from '../services/course.service';
import { ExamService } from '../services/exam.service';

@Component({
  selector: 'app-professor-tasks',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './professor-tasks.component.html',
  styleUrl: './professor-tasks.component.css'
})
export class ProfessorTasksComponent {
  createdByID: any;
  createdBy: any;
  professorCourses : any[] =[];
  assignments : any[] =[];
  exams : any[] =[];
 courseAssignments: { [key: string]: any[] } = {};
courseExams: { [key: string]: any[] } = {};
collapsed: boolean[] = [];

  constructor(private courseService : CourseService, private assignmentService : AssignmentService, private examService : ExamService){
    const currentUserString = sessionStorage.getItem('currentUser');
    if (currentUserString) {
      const currentUser = JSON.parse(currentUserString);
      this.createdByID = currentUser.id;
      this.createdBy = currentUser;
    }
    this.loadAllCourses();
  }

  loadAllCourses(): void {
    this.courseService.getCoursesByprofessorId(this.createdByID).subscribe(
      (courses: Course[]) => {
        this.professorCourses = courses;
        this.collapsed = courses.map(() => true); 
        this.initializeTaskContainers();
      },
      (error) => {
        console.error('Error loading courses:', error);
      }
    );
  }
  toggleCollapse(index: number, courseId: string): void {
    if (this.collapsed[index]) {  // If it's about to expand
        this.getAllAssignments(courseId);
        this.getAllExams(courseId);
    }
    this.collapsed[index] = !this.collapsed[index];  // Toggle the current state
}

  initializeTaskContainers(): void {
    this.professorCourses.forEach(course => {
      this.courseAssignments[course.courseId] = [];
      this.courseExams[course.courseId] = [];
    });
  }
  
  getAllAssignments(courseId: string): void {
    // Only fetch if data has not been loaded yet or needs to be refreshed
    if (!this.courseAssignments[courseId] || this.courseAssignments[courseId].length === 0) {
      this.assignmentService.getAssignmentsByCourseId(courseId).subscribe((data) => {
        this.courseAssignments[courseId] = data;
      }, error => {
        console.error('Failed to load assignments:', error);
        this.courseAssignments[courseId] = [];
      });
    }
  }
  
  getAllExams(courseId: string): void {
    // Only fetch if data has not been loaded yet or needs to be refreshed
    if (!this.courseExams[courseId] || this.courseExams[courseId].length === 0) {
      this.examService.getExamsByCourseId(courseId).subscribe((data) => {
        this.courseExams[courseId] = data;
      }, error => {
        console.error('Failed to load exams:', error);
        this.courseExams[courseId] = [];
      });
    }
  }
  
}