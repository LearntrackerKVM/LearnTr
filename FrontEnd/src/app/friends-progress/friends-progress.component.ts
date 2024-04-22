import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseService } from '../services/course.service';

@Component({
  selector: 'app-friends-progress',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './friends-progress.component.html',
  styleUrl: './friends-progress.component.css'
})
export class FriendsProgressComponent {
  progressData: any;
  friend : any;

  constructor(private courseService: CourseService, private route: ActivatedRoute, private router: Router) {
    this.getinitaldata();
   }

  getinitaldata() {
     this.friend = history.state.friend; // Or however you pass the friend data
    if (this.friend && this.friend.id) {
      this.courseService.getCoursesWithTasksAndMilestonesByStudentId('course',this.friend.id)
        .subscribe(data => {
          this.progressData = data;
        }, error => {
          console.error('Error fetching progress data:', error);
        });
    } else {
      // Handle case where no friend data is available
      console.warn('No friend data provided, redirecting...');
      this.router.navigate(['/some-default-route']);
    }
  }

  // In friends-progress.component.ts

calculateProgress(task: any): string {
  const completedMilestones = task.milestones.filter((m: any) => m.status === 'Completed').length;
  const totalMilestones = task.milestones.length;
  const progressPercentage = (completedMilestones / totalMilestones) * 100;
  return progressPercentage + '%'; // Return as a string to be used directly in the template
}

}
