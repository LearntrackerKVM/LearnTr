import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-askteacher',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './askteacher.component.html',
  styleUrl: './askteacher.component.css'
})
export class AskteacherComponent {
  showAskTeacher : boolean = true; // Initial state to show 'Ask Your Teacher' form
  question = '';
  discussionTitle = '';
  initialPost = '';

  showForm(formType: string) {
    this.showAskTeacher = formType === 'askTeacher';
  }

  submitQuestion() {
    // Handle question submission
  }

  submitDiscussion() {
    // Handle discussion submission
  }
}