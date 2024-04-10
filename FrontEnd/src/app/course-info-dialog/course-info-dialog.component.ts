import { CommonModule } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-course-info-dialog',
  standalone: true,
  imports: [MatDialogModule,MatButtonModule,CommonModule],
  templateUrl: './course-info-dialog.component.html',
  styleUrl: './course-info-dialog.component.css'
})
export class CourseInfoDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: any) { }
}
