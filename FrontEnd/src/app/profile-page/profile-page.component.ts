import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-profile-page',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './profile-page.component.html',
  styleUrl: './profile-page.component.css'
})
export class ProfilePageComponent {
  firstName: string = 'Sai';
  lastName: string = 'Koushik';
  email: string = 'yskoushik@icloud.com';
  editMode: boolean = false;
  major : string = 'Computer Science'

  selectedFile: File | null = null;
  profileImageUrl: SafeUrl | null = null;  // Use SafeUrl to sanitize the image URL

  constructor(private http: HttpClient, private sanitizer: DomSanitizer) {}

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0] as File;
  }

  toggleEditMode() {
    this.editMode = !this.editMode;
  }

  SaveDetails(){
    this.editMode = false;
  }

  CancelDetails(){
    this.editMode = false;
  }

 


  uploadImage(): void {
    if (!this.selectedFile) {
      console.error('No file selected');
      return;
    }

    const imageUrl = `assets/uploads/${this.selectedFile.name}`;

    // Sanitize the URL using DomSanitizer
    this.profileImageUrl = this.sanitizer.bypassSecurityTrustUrl(imageUrl);
  }

  changeProfilePicture(): void {
    const fileInput = document.getElementById('fileInput');
    if (fileInput) {
      fileInput.click();
    }
  }
}