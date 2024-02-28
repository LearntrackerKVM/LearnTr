import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-leader-board',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './leader-board.component.html',
  styleUrl: './leader-board.component.css'
})
export class LeaderBoardComponent {
  courses: string[] = ['Algorithms', 'Software Engineering', 'Networking'];
  courseFilter: string = '';
  filterleaderboardData : any;
  leaderboardData = [
    { name: ' Student 1', progress: 60 ,course : 'Algorithms'},
    { name: ' Student 2', progress: 30 ,course : 'Software Engineering'},
    { name: ' Student 3', progress: 80 ,course : 'Software Engineering'},
    { name: ' Student 4', progress: 10 ,course : 'Algorithms'},
    { name: ' Student 5', progress: 80 ,course : 'Software Engineering'},
    { name: ' Student 6', progress: 20 ,course : 'Software Engineering'},
    { name: ' Student 7', progress: 80 ,course : 'Networking'},
    { name: ' Student 8', progress: 70 ,course : 'Networking'},
    // Add more  Students as needed
  ];
  selectedCourse: string = ''; // Store selected course filter value
  selectedProgressRange: string = ''; // Store selected progress range filter value

  ngOnInit(){
    this.filterleaderboardData = this.leaderboardData;
  }
  // Check if the progress is in the selected range
  isProgressInRange(progress: number): boolean {
    switch (this.selectedProgressRange) {
      case '':
        return true;
      case '0-10':
        return progress >= 0 && progress <= 10;
      case '10-20':
        return progress > 10 && progress <= 20;
      case '20-40':
        return progress > 20 && progress <= 40;
      case '40-50':
        return progress > 40 && progress <= 50;
      case '50-70':
        return progress > 50 && progress <= 70;
      case '70+':
        return progress > 70;
      default:
        return false;
    }
  }
  applyFilters() {
    debugger
    // Logic to filter the data based on selectedCourse and selectedProgressRange
    // This logic may vary based on your application's requirements
    this.filterleaderboardData = this.leaderboardData.filter( Student =>
      (this.selectedCourse === '' ||  Student.course === this.selectedCourse) &&
      (this.selectedProgressRange === '' || this.isProgressInRange( Student.progress))
    );
  }
}
