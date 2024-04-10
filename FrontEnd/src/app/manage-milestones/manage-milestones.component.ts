import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AssignmentService } from '../services/assignment.service';
import { StudentTasksService } from '../services/student-tasks.service';
import { TaskMileStonesService } from '../services/task-mile-stones.service';
import { ChangeDetectorRef } from '@angular/core';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CompleteMilestoneDialogComponentComponent } from '../complete-milestone-dialog-component/complete-milestone-dialog-component.component';
import { DiscoverInsightsComponent } from '../discover-insights/discover-insights.component';


@Component({
  selector: 'app-manage-milestones',
  standalone: true,
  imports: [CommonModule,FormsModule,MatDialogModule,CompleteMilestoneDialogComponentComponent],
  templateUrl: './manage-milestones.component.html',
  styleUrl: './manage-milestones.component.css'
})
export class ManageMilestonesComponent {
  task: any;
  milestones : any = [];
  showCompleteBox : boolean = false;
  showNotesBox : boolean = false;

  constructor(private route: ActivatedRoute, private router: Router,private taskMilestoneService : TaskMileStonesService,private assignmentService: AssignmentService,private studentTasksService : StudentTasksService,private cdr: ChangeDetectorRef,public dialog: MatDialog) { 
    this.task = history.state.task;

    // If that doesn't work, as a fallback, you can try accessing the state from the ActivatedRoute
    if (!this.task) {
      this.task = this.route.snapshot.paramMap.get('task');
    }
    this.fetchMilestones();
  }

  ngOnInit(): void {

  }

  
  
  saveTaskChanges() {
    // Check if the task object is defined
    if (!this.task) {
      console.error('Task data is undefined');
      return;
    }

    // Prepare the data for updating. Assume task.id, task.difficultyLevel, task.notes, and task.status are available
    // The completionDate field is optional in your service method
    const completionDate = this.task.completionDate ? this.formatDate(this.task.completionDate) : undefined;

    // Call the service method to update the task
    this.studentTasksService.updateStudentTask(
      this.task.id,
      this.task.difficultyLevel,
      this.task.notes,
      this.task.status,
      completionDate
    ).subscribe({
      next: (response) => {
        // Handle successful update here
        console.log('Task updated successfully:', response);
        // Optionally, trigger any UI updates or redirections here
      },
      error: (error) => {
        // Handle error case
        console.error('Failed to update task:', error);
      }
    });
  }

  // Helper method to format the date as a string if necessary
  // Adapt this method based on how your backend expects to receive the date string
  formatDate(date: Date): string {
    // Example formatting, adjust as needed
    return date.toISOString().substring(0, 10); // Outputs date in 'YYYY-MM-DD' format
  }


  onFieldChange(){

  }

  fetchMilestones(): void {
    this.taskMilestoneService.getMilestonesByStudentTaskId(this.task.id).subscribe({
      next: (data) => this.milestones = data,
      error: (err) => console.error('Failed to fetch milestones', err)
    });
  }

  onMilestoneCompletionChange(m : any){

  }

  downloadFile(): void {
    if (this.task && this.task.taskId && this.task.courseId && this.task.title) {
      this.assignmentService.downloadAssignmentFile( this.task.courseId, this.task.title)
        .subscribe(blob => {
          // Create a new Blob object using the 
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = this.task.title.replace(/\s+/g, '_') + '.pdf'; // You might want to dynamically determine the file extension
          document.body.appendChild(a);
          a.click();
          window.URL.revokeObjectURL(url);
          a.remove();
        }, error => {
          console.error('Error downloading file:', error);
          // Handle the error, possibly by showing an error message to the user
        });
    } else {
      console.error('Task details are incomplete. Cannot download the file.');
      // Handle this case as needed, perhaps by showing an alert or notification to the user
    }
  }
  openCompleteBox(milestone: any) {
    this.showCompleteBox = true;
    const dialogRef = this.dialog.open(CompleteMilestoneDialogComponentComponent, {
      width: '900px',
      data: { milestone: milestone }
    });
  
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      // Handle result if needed
    });
  }
  openInsightsBox(milestone: any) {
    const dialogRef = this.dialog.open(DiscoverInsightsComponent, {
      width: '900px',
      data: { milestone: milestone }
    });
  
    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      // Handle result if needed
    });
  }

  markAsComplete(milestone: any) {
    milestone.isComplete = true;
    // Close the modal/box
    milestone.showCompleteBox = false;
  }

  saveDifficulty(milestone: any) {
    // Here you would save the difficulty level to your backend or handle it as needed
    console.log(`Saved difficulty level: ${milestone.difficultyLevel}`);
    // Optionally close the modal/box
    milestone.showCompleteBox = false;
  }

  saveNotes(milestone: any) {
    // Here you would save the notes to your backend or handle it as needed
    console.log(`Saved notes: ${milestone.notes}`);
    // Close the modal/box
    milestone.showNotesBox = false;
  }

}

