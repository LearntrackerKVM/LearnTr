import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

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

  courses: any[] = [
    { id: 1, name: 'Course A' },
    { id: 2, name: 'Course B' },
    // Add more courses as needed
  ];
  selectedCourse: number | null = null;

  assignments: any[] = [];
  selectedAssignment: number | null = null;
  customReminderTime: string = '00:00';

  customReminders: any[] = [];

  loadAssignments() {
    if (this.selectedCourse == 1) {
      this.assignments = [
        { id: 1, name: 'Assignment 1' },
        { id: 2, name: 'Assignment 2' },
      ];
    } else  if (this.selectedCourse == 2){
      this.assignments = [
        { id: 1, name: 'Assignment 3' },
        { id: 2, name: 'Assignment 4' },
      ];

    }
      else{
      this.assignments = [];
    }
    this.selectedAssignment = null;
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