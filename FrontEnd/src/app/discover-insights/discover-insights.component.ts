import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { TaskMileStonesService } from '../services/task-mile-stones.service';

type DifficultyLevel = 'low' | 'moderate' | 'high';
const difficultyMapping: { [key in DifficultyLevel]: number } = {
  low: 1,
  moderate: 2,
  high: 3,
};
@Component({
  selector: 'app-discover-insights',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './discover-insights.component.html',
  styleUrl: './discover-insights.component.css'
})
export class DiscoverInsightsComponent {
  milestoneUserInfos : any[] =[];
  createdByID: any;
  createdBy: any;
  avgDiffLevel : string = '';

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,private taskMilestoneService : TaskMileStonesService){
    const currentUserString = sessionStorage.getItem('currentUser');
    if (currentUserString) {
      const currentUser = JSON.parse(currentUserString);
      this.createdByID = currentUser.id;
      this.createdBy = currentUser.userName;
    }

    this.getMilestonesWithUserInfo(data.milestone.studentTaskId,data.milestone.title)
  }
  getMilestonesWithUserInfo(studentTaskId: string, title: string) {
    this.taskMilestoneService.getMilestonesWithUserInfo(studentTaskId, title)
      .subscribe(data => {
        this.milestoneUserInfos = data;
        this.avgDiffLevel = this.calculateAverageDifficulty(this.milestoneUserInfos)
        console.log(this.milestoneUserInfos);
      }, error => {
        console.error('Error fetching milestone user info:', error);
      });
  }
  calculateAverageDifficulty(milestoneUserInfos: any[]): string {
    let totalDifficulty = 0;
    milestoneUserInfos.forEach(info => {
      const difficultyLevel: DifficultyLevel = info.milestone.difficultyLevel.toLowerCase() as DifficultyLevel;
      // Now TypeScript knows difficultyLevel is a valid key of difficultyMapping
      totalDifficulty += difficultyMapping[difficultyLevel] || 0;
    });
  
    const averageDifficultyValue = totalDifficulty / milestoneUserInfos.length;
  
    // Mapping back to a qualitative measure, simplified for demonstration
    let averageDifficultyLevel = '';
    if (averageDifficultyValue <= 1) {
      averageDifficultyLevel = 'Low';
    } else if (averageDifficultyValue <= 2) {
      averageDifficultyLevel = 'Moderate';
    } else {
      averageDifficultyLevel = 'High';
    }
  
    return averageDifficultyLevel;
  }
  
}
