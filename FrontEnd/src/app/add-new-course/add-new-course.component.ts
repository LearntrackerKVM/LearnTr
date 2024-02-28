import { CommonModule } from '@angular/common';
import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';

interface Assignment {
  assignmentNumber: number | null;
  assignmentName: string;
  dueDate: string;
  numberOfQuestions: number;
}
export interface ClassSchedule {
  dayOfWeek: string;
  startTime: string;
  endTime: string;
}

@Component({
  selector: 'app-add-new-course',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './add-new-course.component.html',
  styleUrl: './add-new-course.component.css'
})
export class AddNewCourseComponent {
  @ViewChild('buttonContainer', { static: true })
  buttonContainer!: ElementRef;
  sliderStyle = {};
  activeButton: string = 'Courses';
  courses = [
    { name: 'Course 1', assignments: [{ number: 1, name: 'Assignment 1.1', dueDate: '2024-03-01', numberOfQuestions: 5 }] },
    // Add more courses and their assignments here
  ];
  announcements: any[] = [
    // existing announcements
  ];
  newAnnouncement = {
    title: '',
    content: ''
  };
  availableAssignments = [];
  selectedCourse = '';
  selectedAssignment!: Assignment;
  course = {
    courseName: '',
    description: '',
    professor: '',
    startDate: '',
    endDate: '',
    assignments: 0,
    exams: 0,
    capacity: 0,
    schedules: []
  };
  
  classSchedules: ClassSchedule[] = [];

  constructor(){
    this.selectedAssignment = {
      assignmentNumber: null,
      assignmentName: '',
      dueDate: '',
      numberOfQuestions: 0
    };
  }

  onCourseChange() {
    const course = this.courses.find(course => course.name === this.selectedCourse);
    this.resetSelectedAssignment();
  }
  addAnnouncement() {
    this.announcements.push(this.newAnnouncement);
    this.newAnnouncement = { title: '', content: '' }; // Reset for next entry
  }

  addSchedule() {
    this.classSchedules.push({
      dayOfWeek: '',
      startTime: '',
      endTime: '',
    });
  }

  removeSchedule(index: number) {
    this.classSchedules.splice(index, 1);
  }

  onAssignmentChange() {
  
  }

  resetSelectedAssignment() {
    this.selectedAssignment = {
      assignmentNumber: null,
      assignmentName: '',
      numberOfQuestions: 0,
      dueDate: ''
    };
  }

  setActive(buttonName: string, event: any): void {
    this.activeButton = buttonName;
    this.moveSlider(event.target);
  }

  moveSlider(button: HTMLElement) {
    const sliderWidth = button.offsetWidth;
    const sliderOffset = button.offsetLeft;
    this.sliderStyle = {
      width: `${sliderWidth}px`,
      transform: `translateX(${sliderOffset}px)`
    };
  }

  ngAfterViewInit() {
    // Set the slider to the initial button on load
    const initialButton = this.buttonContainer.nativeElement.children[1]; // Assuming the first button is "Courses"
    this.moveSlider(initialButton);
  }

  onSubmit() {
    console.log(this.course);
  }
}