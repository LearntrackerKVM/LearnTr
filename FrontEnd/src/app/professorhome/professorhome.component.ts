import { CommonModule } from '@angular/common';
import { Component, NO_ERRORS_SCHEMA } from '@angular/core';
import { ProgressCircleComponent } from '../progress-circle/progress-circle.component';

@Component({
  selector: 'app-professorhome',
  standalone: true,
  imports: [CommonModule,ProgressCircleComponent],
  schemas:[NO_ERRORS_SCHEMA],
  templateUrl: './professorhome.component.html',
  styleUrl: './professorhome.component.css'
})
export class ProfessorhomeComponent {
  professorCourses: any[] = [
  ];
  courses = [
    { addedDate: '2024-02-15', studentsEnrolled: 25, courseName: 'Angular Basics' },
    {  addedDate: '2024-02-16', studentsEnrolled: 20, courseName: 'Advanced JavaScript' },
    { addedDate: '2024-02-17', studentsEnrolled: 15, courseName: 'Web Development Fundamentals' }

  ];
  isFlipped: boolean = false;
  progressValue = 50; // Seng t the initial progress value
  currentProgressValue: number = 70;
  // Function to update the progress value
  updateProgress() {
    this.progressValue += 10;
    if (this.progressValue > 100) {
      this.progressValue = 0; // Reset when reaching 100%
    }
  }
  flipCard() {
    this.isFlipped = !this.isFlipped;
  }

  addCourse() {

  }
}
