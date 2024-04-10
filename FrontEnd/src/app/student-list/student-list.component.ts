import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Course } from '../models/Course';
import { CourseService } from '../services/course.service';

@Component({
  selector: 'app-student-list',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './student-list.component.html',
  styleUrl: './student-list.component.css'
})
export class StudentListComponent {

  currentUser: any;
  createdByID: any;
  createdBy: any;
  courses: any = [];
  selectedCourse: string = "allCourses"; 
  studentList: any = [];
  filteredStudentList: any = [];


  constructor(private courseService: CourseService) {
    const currentUserString = sessionStorage.getItem('currentUser');
    if (currentUserString) {
      this.currentUser = JSON.parse(currentUserString);
      this.createdByID = this.currentUser.id;
      this.createdBy = this.currentUser.userName;
    }
    this.loadAllCourses();
    this.loadStudentList();
  }
  loadAllCourses(): void {
    this.courseService.getCoursesByprofessorId(this.createdByID).subscribe(
      (courses: Course[]) => {
        this.courses = courses;
      },
      (error) => {
        console.error('Error loading courses:', error);
      }
    );
  }
  loadStudentList() {
    this.courseService.getAllStudentsEnrolledInCoursesAddedByProfessor(this.createdByID).subscribe((students) => {
        this.studentList = students;
        this.filteredStudentList = students;

        students.forEach((student: any) => {
            this.courseService.getCoursesForStudent(student.id).subscribe((courses) => {
                student.courses = courses;
            });
        });
    });
}
getCoursesNames(courses: any[]): string {
  return courses.map(course => course.courseName).join(', ');
}


filterData(): void {
  // Adjust your filtering logic based on the selected course
  if (this.selectedCourse === 'allCourses') {
    this.filteredStudentList = this.studentList;
  } else {
    this.filteredStudentList = this.studentList.filter((student: any) =>
      this.hasCourse(student, this.selectedCourse)
    );
  }
}

hasCourse(student: any, courseId: string): boolean {
  return student.courses.some((course: any) => course.courseId === courseId);
}

  sortData(field: string): void {
    this.filteredStudentList.sort((a: any, b: any) => {
      const valA = (typeof a[field] === 'string') ? a[field].toUpperCase() : a[field];
      const valB = (typeof b[field] === 'string') ? b[field].toUpperCase() : b[field];
      return (valA < valB) ? -1 : (valA > valB) ? 1 : 0;
    });
  }

}
