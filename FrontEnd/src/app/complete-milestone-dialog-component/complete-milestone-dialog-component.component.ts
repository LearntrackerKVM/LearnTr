import { Component, Inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import confetti from 'canvas-confetti';


import { PopupService } from '../services/popup.service';
import { TaskMileStonesService } from '../services/task-mile-stones.service';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-complete-milestone-dialog-component',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './complete-milestone-dialog-component.component.html',
  styleUrl: './complete-milestone-dialog-component.component.css'
})
export class CompleteMilestoneDialogComponentComponent {

previousBadge : string = '';
newBadge : string = '';
  createdByID: any;
  createdBy: any;
  constructor(@Inject(MAT_DIALOG_DATA) public data: any,private taskMileStoneServie : TaskMileStonesService,private userService : UserService,  private popupService: PopupService,
  private dialogRef: MatDialogRef<CompleteMilestoneDialogComponentComponent>, ) {
    const currentUserString = sessionStorage.getItem('currentUser');
    if (currentUserString) {
      const currentUser = JSON.parse(currentUserString);
      this.createdByID = currentUser.id;
      this.createdBy = currentUser.email;
      this.previousBadge =currentUser.badge;
    }
  }

  save(){
    
      const milestoneId = this.data.milestone.id;
      const updatedMilestone = this.data.milestone;
  
      this.taskMileStoneServie.updateMilestone(milestoneId, updatedMilestone).subscribe({
        next: (response) => {
          this.checkNewBadge();
          console.log('Milestone updated successfully', response);
        },
        error: (error) => {
          console.error('Error updating milestone', error);
        }
      });
    }

    checkNewBadge(){
      this.userService.getUserByEmail(this.createdBy).subscribe(response => {
        this.newBadge = response.badge
        if(this.previousBadge != this.newBadge){
          this.createConfetti();
          this.popupService.show(`Congratulations on earning the ${this.newBadge} badge! For more info, visit the "badges info" page.`);
          this.closeDialog();
        }
        else{   
          this.popupService.show(`Well done! milestone completed`);
          this.closeDialog();
        }
      })
      
    
    }
    closeDialog() {
      this.dialogRef.close();
    }
    createConfetti() {
      confetti({
        particleCount: 300, // A reasonable number for performance
        spread: 360, // A full circle
        startVelocity: 30, // How fast the confetti starts off, can adjust for effect
        origin: { x: 0.5, y: 0 }, // Start from the top middle of the screen
      });
    }
    
}

