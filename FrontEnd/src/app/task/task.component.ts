import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { Task } from '../models/Task';
import { CourseService } from '../services/course.service';
import { StudentTasksService } from '../services/student-tasks.service';

@Component({
  selector: 'app-task',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.css']
})
export class TaskComponent implements OnInit {
  tasks: Task[] = [];
  filteredTasks: Task[] = [];
  filterType = '';
  filterDueDate = '';
  selectedCourseId : string = ''; // Updated for clarity
  myCourses: any = [];
  createdByID: any;

  constructor(private studentTasksService: StudentTasksService, private router: Router, private courseService: CourseService) {
    const currentUserString = sessionStorage.getItem('currentUser');
    if (currentUserString) {
      const currentUser = JSON.parse(currentUserString);
      this.createdByID = currentUser.id;
    }
  }

  ngOnInit() {
    this.loadTasks();
    this.getStudentCourses();
  }

  loadTasks(): void {
    this.studentTasksService.getAllStudentTasksByStudentId(this.createdByID).subscribe(tasks => {
      this.tasks = tasks;
      this.filteredTasks = tasks;
     // Initially, no filters are applied
    });
  }

  applyFilters(): void {
    this.filteredTasks = this.tasks.filter(task => {
      const matchesType = this.filterType ? task.taskType.includes(this.filterType) : true;
      // Convert selectedCourseId to number for comparison
      const matchesCourse = this.selectedCourseId ? task.courseId.toString() === this.selectedCourseId : true;
      const matchesDate = this.filterDueDate ? new Date(task.dueDate).toISOString().slice(0, 10) === this.filterDueDate : true;
      return matchesType && matchesCourse && matchesDate;
    });
  }
  
  resetFilters(){
    this.filteredTasks = this.tasks;
    this.selectedCourseId = '';
  }

  manageTask(task: any): void {
    this.router.navigate(['/milestones', task.id], { state: { task: task } });
  }

  getStudentCourses() {
    this.courseService.getCoursesForStudent(this.createdByID).subscribe((courses) => {
      this.myCourses = courses;
    });
  }
}
