
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Task } from '../models/Task';
import { CourseService } from '../services/course.service';
import { StudentTasksService } from '../services/student-tasks.service';
import { ProgressCircleComponent } from '../progress-circle/progress-circle.component';
import { NgCircleProgressModule } from 'ng-circle-progress';
import { Router } from '@angular/router';

@Component({
  selector: 'app-studentcourses',
  standalone: true,
  imports: [CommonModule, FormsModule, NgCircleProgressModule],
  templateUrl: './studentcourses.component.html',
  styleUrl: './studentcourses.component.css'
})
export class StudentcoursesComponent implements OnInit {
  currentUser: any;
  studentCourses: any[] = [];
  tasks: any[] = [];
  filteredTasks: any[] = [];
  progresstasks: any[] = [];
  availableCourses: any[] = [];
  showAssignmentTable: boolean = true;
  showExamTable: boolean = false;
  currentFilter: 'upcoming' | 'overdue' | 'completed' = 'upcoming'; // Default to 'upcoming'
  currentTaskType: 'Assignment' | 'Exam' = 'Assignment';
  selectedCourse: string = '';
  progressData: any[] = [];
  milestones: any[] = [];

  constructor(private coursesService: CourseService, private studentTasksService: StudentTasksService, private router: Router) {
    const currentUserString = sessionStorage.getItem('currentUser');
    if (currentUserString) {
      this.currentUser = JSON.parse(currentUserString);
    }
    this.loadTprogressasks();
  }

  ngOnInit(): void {
    this.loadStudentCourses();
    this.loadTasks();

  }

  loadTprogressasks(): void {
    this.progresstasks = [
      { title: 'Task 1', progress: 25, color: '#c4e7f7' },
      { title: 'Task 2', progress: 50, color: '#E6E6FA' },
      { title: 'Task 3', progress: 75, color: '#FADADD' },
      { title: 'Task 4', progress: 100, color: '#FFDAB9' }
    ];


  }
  loadStudentCourses() {
    this.coursesService.getCoursesForStudent(this.currentUser.id).subscribe((response) => {
      this.studentCourses = response;
      this.getAllCourses();
    })
  }

  loadTasks(): void {
    this.studentTasksService.getAllStudentTasksByStudentId(this.currentUser.id).subscribe(tasks => {
      this.tasks = tasks;
      this.filterTasks('upcoming'); // Filter tasks as upcoming on load
      this.updateTableVisibility(); // Ensure table visibility is correct based on filtered tasks
    });
  }

  showTaskInfo(task: any): void {
    console.log('Task Info:', task);
  }

  updateTableVisibility() {
    const hasAssignments = this.filteredTasks.some(task => task.taskType === 'Assignment');
    const hasExams = this.filteredTasks.some(task => task.taskType === 'Exam');

    // Update visibility based on the presence of tasks and the currently selected task type
    this.showAssignmentTable = hasAssignments && this.currentTaskType === 'Assignment';
    this.showExamTable = hasExams && this.currentTaskType === 'Exam';
  }



  getAllCourses() {
    this.coursesService.getAllCourses().subscribe((courses) => {
      this.availableCourses = courses;
      this.updateStudentCourses();
    }, (error) => {
      console.error('Error loading courses:', error);
    });
  }
  filterTasks(filterType: string): void {
    const currentDate = new Date();
    this.filteredTasks = this.tasks.filter(task => {
      const dueDate = new Date(task.dueDate);
      return (filterType === 'upcoming' ? dueDate > currentDate && task.status !== 'Completed' :
        filterType === 'overdue' ? dueDate < currentDate && task.status !== 'Completed' :
          task.status === 'Completed') && // Checks the status
        task.taskType === this.currentTaskType && // Checks the type (Assignment or Exam)
        (this.selectedCourse === '' || task.courseId === this.selectedCourse); // Checks the course filter
    });
    this.updateTableVisibility();
  }

  updateCourseFilter(event: any): void {
    this.selectedCourse = event.target?.value;
    this.filterTasks('upcoming'); // Reapply current filters with new course selection
  }
  updateStudentCourses() {
    // Update studentCourses by adding data from availableCourses without duplication
    this.studentCourses = this.studentCourses.map(studentCourse => {
      // Find the corresponding course in availableCourses
      const course = this.availableCourses.find(ac => ac.courseId === studentCourse.courseId);
      if (course) {
        // Merge additional information from availableCourses into studentCourse
        return {
          ...studentCourse, // original student course data
          ...{
            courseDescription: course.courseDescription,
            noOfAssignments: course.noOfAssignments,
            noOfExams: course.noOfExams,
            startDate: course.startDate,
            endDate: course.endDate,
            noOfStudentsEnrolled: course.noOfStudentsEnrolled,
            createdBy: course.createdBy,
            createdByID: course.createdByID,
            capacity: course.capacity,
            createdAt: course.createdAt,
            semester: course.semester,
          }
        };
      }
      return studentCourse; // Return the original if no match found
    });
  }
  filterTasksByType(): Task[] {
    return this.tasks.filter(task => task.taskType === 'Exam' || task.taskType === 'Assignment');

  }

  calculateDaysUntilDue(dueDate: string): number {
    const now = new Date();
    const due = new Date(dueDate);
    return Math.ceil((due.getTime() - now.getTime()) / (1000 * 3600 * 24));
  }

  getCourseColor(courseId: string): string | undefined {
    const course = this.studentCourses.find(course => course.courseId === courseId);
    return course?.color;
  }
  manageTask(task: any): void {
    this.router.navigate(['/milestones', task.id], { state: { task: task } });
  }
  filterUpcomingTasks() {
    this.filteredTasks = this.tasks.filter(task => new Date(task.dueDate) > new Date() && task.status !== 'Completed');
  }

  filterOverdueTasks() {
    this.filteredTasks = this.tasks.filter(task => new Date(task.dueDate) < new Date() && task.status !== 'Completed');
  }

  filterCompletedTasks() {
    this.filteredTasks = this.tasks.filter(task => task.status === 'Completed');
  }

  setFilter(filter: 'upcoming' | 'overdue' | 'completed'): void {
    this.currentFilter = filter;
    this.filterTasks(filter);
  }

  toggleTaskType(type: 'Assignment' | 'Exam'): void {
    this.currentTaskType = type;
    this.filterTasks(this.currentFilter); // Apply the current filter with new task type
  }
  getTasksByCourse(courseId: string) {
    // This will now correctly filter tasks regardless of type, based on the currentTaskType
    return this.filteredTasks.filter(task => task.courseId === courseId && (!this.currentTaskType || this.currentTaskType === task.taskType));
}

calculateProgress(task: any) {
    // The method calculates progress based on the completion of milestones
    if (task.milestones > 0) {
        return (task.milestonesCompleted / task.milestones) * 100;
    }
    return 0; // Return 0 if no milestones or task is not 'Upcoming'
}

getCompletedMilestonesForTask(taskId: string) {
    // This remains the same as previously defined
    return this.milestones.filter(m => m.taskId === taskId && m.isComplete).length;
}


}
