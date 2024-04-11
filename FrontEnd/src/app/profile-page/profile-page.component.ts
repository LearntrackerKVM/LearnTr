
import { CommonModule } from '@angular/common';
import { HttpEventType } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DomSanitizer } from '@angular/platform-browser';
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
    milestonesCompleted : 0,
    profilePicture : ''
  };
  editMode : boolean = false;
  profileImageUrl : boolean = false;
  userProfilePictureUrl : any;

  constructor(private userService: UserService,private sanitizer: DomSanitizer) {
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
      this.loadProfilePicture(this.currentUser.id);
    })
  }

  updateUserProfile(): void {
    this.updateDetails();
    if (this.selectedFile) {
      this.userService.uploadProfilePicture(this.currentUser.id, this.selectedFile).subscribe(event => {
        if (event.type === HttpEventType.Response) {
          this.loadProfilePicture(this.currentUser.id);
        }
      }, error => {
        console.error('Error occurred while uploading profile picture:', error);
      });
    } else {
      // Handle case where no file is selected, if necessary
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
  loadProfilePicture(userId: string): void {
    this.userService.getProfilePicture(userId).subscribe(blob => {
      const objectURL = URL.createObjectURL(blob);
      this.userProfilePictureUrl = this.sanitizer.bypassSecurityTrustUrl(objectURL);
      console.log(this.userProfilePictureUrl);
    }, error => {
      console.error('Failed to load profile picture:', error);
      this.userProfilePictureUrl = this.defaultProfilePicture;
    });
  }
}
