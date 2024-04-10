
import { CommonModule } from '@angular/common';
import { HttpEventType } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-profile-page',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css'
})

export class ProfilePageComponent {
  selectedFile: any;
  userProfilePicture: string | ArrayBuffer = '';
  defaultProfilePicture = 'assets/icons/user.png';
  email : string = ''
  currentUser : any;
  user = {
    id : '',
    role : '',
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    userName: '',
    aboutMe: '',
    rank: '',
    badge: '',
    milestonesCompleted : 0
  };
  editMode : boolean = false;
  profileImageUrl : boolean = false;

  constructor(private userService: UserService) {
    const currentUserString = sessionStorage.getItem('currentUser');
    if (currentUserString) {
      this.currentUser = JSON.parse(currentUserString);
      this,this.email = this.currentUser.email;
    }
    this.getUserDetails();
  }

  onFileSelected(event : any): void {
    this.selectedFile = event.target.files[0] as File;
  }
  getUserDetails(){
    this.userService.getUserByEmail(this.email).subscribe((user) => {
      this.user = user;
    })
  }

  updateUserProfile(): void {
    if (this.selectedFile) {
      this.userService.uploadProfilePicture(this.currentUser.id, this.selectedFile).subscribe(event => {
        if (event.type === HttpEventType.Response) {
          this.userProfilePicture = event.body.profilePictureUrl; // Use the URL from the backend response
        }
      }, error => {
        console.error('Error occurred while uploading profile picture:', error);
      });
    } else {
      this.updateDetails();
    }
  }
  
  updateDetails() {
    this.userService.updateUser(this.user).subscribe({
      next: () => console.log('User updated successfully'),
      error: (error) => console.error('Error updating user', error)
    });
  }

  loadUserProfile(): void {
    // Implement loading the user profile details and picture
  }
  changeProfilePicture(){

  }
  updateUserDetails(){

  }

  cancelEdit(){

  }
}
