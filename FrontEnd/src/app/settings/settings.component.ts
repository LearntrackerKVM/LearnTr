import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Task } from '../models/Task';
import { CourseService } from '../services/course.service';
import { StudentTasksService } from '../services/student-tasks.service';

@Component({
  selector: 'app-settings',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './settings.component.html',
  styleUrl: './settings.component.css'
})
export class SettingsComponent {
  displayProgressToOthers : boolean =false;
  defaultReminderOptions: number[] = Array.from({ length: 31 }, (_, i) => i); // 0 to 30 days
  defaultReminderDays: number = 0;
  customReminderDays: number = 0;
  defaultReminderTime: string = '00:00';
  profileDisplayOption: string = 'all'; 
  studentCourses : any[] = [];
  currentUser: any;
  createdByID: any;
  createdBy: any;
  customReminders: any;
  selectedAssignment: any;
  customReminderTime: any;
  selectedCourse: any;
  assignments : any;
  tasks: Task[] = [];
  filteredTasks: Task[] = [];
  filterType: any;
  filterDueDate: any;

  constructor(private coursesService : CourseService,private studentTasksService : StudentTasksService){
    const currentUserString = sessionStorage.getItem('currentUser');
    if (currentUserString) {
      this.currentUser = JSON.parse(currentUserString);
      this.createdByID = this.currentUser.id;
      this.createdBy = this.currentUser.userName;
    }
  this.loadStudentCourses();
  }

  loadStudentCourses() {
    this.coursesService.getCoursesForStudent(this.createdByID).subscribe((response) => {
      this.studentCourses = response;
      this.loadTasks();
    })
  }
  loadTasks(): void {
    this.studentTasksService.getAllStudentTasksByStudentId(this.createdByID).subscribe(tasks => {
      this.tasks = tasks;
      this.filteredTasks = tasks;
    });
  }

  applyFilters(): void {
    this.filteredTasks = this.tasks.filter(task => {
      const matchesCourse = this.selectedCourse ? task.courseId.toString() === this.selectedCourse : true;
      return matchesCourse;
    });
  }
  
  addCustomReminder() {
    this.customReminders.push({
      course: this.selectedCourse,
      assignment: this.selectedAssignment,
      daysBefore: this.customReminderDays,
      time: this.customReminderTime
    });
    this.customReminderDays = 0;
    this.customReminderTime = '00:00';
  }
}