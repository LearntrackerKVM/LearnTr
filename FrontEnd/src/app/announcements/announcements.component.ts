import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Announcement } from '../models/Announcement';
import { AnnouncementService } from '../services/announcement.service';
import { CourseService } from '../services/course.service';

@Component({
  selector: 'app-announcements',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './announcements.component.html',
  styleUrl: './announcements.component.css'
})
export class AnnouncementsComponent {
  currentUser: any;
  showannoucemnet : boolean = false;
  announcements : Announcement[] = [];
  studentCourses: any = [];
  selectCourseId : any;

  constructor( private coursesService: CourseService,private announcementService : AnnouncementService
   ) {
    const currentUserString = sessionStorage.getItem('currentUser');
    if (currentUserString) {
      this.currentUser = JSON.parse(currentUserString);
    }
    this.loadStudentCourses();
  }

  getAllAnnouncements(id : any) {
    this.announcementService.getAnnouncementsByCourseId(id)
      .subscribe(
        (data: Announcement[]) => {
          this.announcements = data;
        },
        error => {
          console.error('Error fetching announcements: ', error);
          // Handle error as needed
        }
      );
  }

  filterAnnouncemnets(event: any) {

    const courseID = event.target?.value;
    this.showannoucemnet = true;
    this.getAllAnnouncements(courseID);
  }

  loadStudentCourses() {
    this.coursesService.getCoursesForStudent(this.currentUser.id).subscribe((response) => {
      this.studentCourses = response;
    })
  }
}
