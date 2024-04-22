import { CommonModule } from '@angular/common';
import { Component, NO_ERRORS_SCHEMA } from '@angular/core';
import { Course } from '../models/Course';
import { CourseSchedule } from '../models/CourseSchedule';
import { ProgressCircleComponent } from '../progress-circle/progress-circle.component';
import { AssignmentService } from '../services/assignment.service';
import { CourseService } from '../services/course.service';
import { ExamService } from '../services/exam.service';

interface WeekSchedule {
  [day: string]: { timeRange: string, courseName: string }[];
}

@Component({
  selector: 'app-professorhome',
  standalone: true,
  imports: [CommonModule,ProgressCircleComponent],
  schemas:[NO_ERRORS_SCHEMA],
  templateUrl: './professorhome.component.html',
  styleUrl: './professorhome.component.css'
})
export class ProfessorhomeComponent {
  professorCourses: any[] = [
  ];
  isFlipped: boolean = false;
  progressValue = 50;
  currentProgressValue: number = 70;
  createdBy : string = '';
  createdByID : number = 0;
  courseswithSchedules : any[] = [];
    cards = [
      {
        icon: '/assets/icons/courses.png',
        number: 0,
        title: 'Courses Added',
        color: '#c4e7f7',
        hoverColor: ''
      },
      {
        icon: '/assets/icons/students.png',
        number: 0,
        title: 'Students Enrolled',
        color: '#E6E6FA',
        hoverColor: ''
      },
      {
        icon: '/assets/icons/assignments.png',
        number: 0,
        title: 'Assignments Added',
        color: '#FADADD',
        hoverColor: ''
      },
      {
        icon: '/assets/icons/exams.png',
        number: 0,
        title: 'Exams Added',
        color: '#FFDAB9',
        hoverColor: ''
      }
    ];

  constructor(private courseService : CourseService,private assignmentService : AssignmentService,private examService : ExamService){
    const currentUserString = sessionStorage.getItem('currentUser');
    if (currentUserString) {
      const currentUser = JSON.parse(currentUserString);
      this.createdByID = currentUser.id;
      this.createdBy = currentUser.userName;
    }

    this.loadAllCourses();
    this.getDefaultNumbers();

  }
  getDefaultNumbers() {
    this.courseService.getNoOfCoursesAddedByProfessor(this.createdByID.toString()).subscribe(num => {
      this.updateCardNumber('Courses Added', num);
    });

    this.courseService.getNumberOfStudentsEnrolled(this.createdByID.toString()).subscribe(num => {
      this.updateCardNumber('Students Enrolled', num);
    });

    this.assignmentService.getNoOfAssignmentsAddedByProfessor(this.createdByID.toString()).subscribe(num => {
      this.updateCardNumber('Assignments Added', num);
    });

    this.examService.getNoOfExamsAddedByProfessor(this.createdByID.toString()).subscribe(num => {
      this.updateCardNumber('Exams Added', num);
    });
  }

  updateCardNumber(title: string, num: number) {
    const cardIndex = this.cards.findIndex(card => card.title === title);
    if (cardIndex !== -1) {
      this.cards[cardIndex].number = num;
    }
  }
  loadAllCourses(): void {
    this.courseService.getCoursesByprofessorId(this.createdByID).subscribe(
      (courses: Course[]) => {
        this.professorCourses = courses;
        // Convert courseIds from number to string, filtering out undefined values.
        const courseIds: string[] = courses
          .map(course => course.courseId ? course.courseId.toString() : undefined) // Convert number to string
          .filter((courseId): courseId is string => typeof courseId === 'string'); // Filter out undefined
  
        this.loadCoursesSchedules(courseIds,courses);
      },
      (error) => {
        console.error('Error loading courses:', error);
      }
    );
  }
  
  setHoverColor(card: any, event: boolean) {
    if (event) {
      card.hoverColor = card.color;  // on mouse enter
    } else {
      card.hoverColor = '#fff';  // on mouse leave, revert to original
    }
  }
  loadCoursesSchedules(courseIds: string[],courses:Course[]): void {
    this.courseService.getCoursesSchedulesByCourseIds(courseIds).subscribe(
      (schedules: CourseSchedule[]) => {
        this.courseswithSchedules = this.professorCourses.map(course => {
          const filteredSchedules = schedules.filter(schedule => schedule.courseId === course.courseId);
          return {
            ...course,
            weekSchedule: this.transformSchedulesForWeek(filteredSchedules,courses),
          };
        });
      },
      (error) => {
        console.error('Error loading schedules:', error);
      }
    );
  }
  
  transformSchedulesForWeek(schedules: CourseSchedule[], courses: Course[]): WeekSchedule {
    const weekSchedule: WeekSchedule = {
      Monday: [],
      Tuesday: [],
      Wednesday: [],
      Thursday: [],
      Friday: [],
    };
  
    schedules.forEach(schedule => {
      const courseName = courses.find(course => course.courseId === schedule.courseId)?.courseName || 'Unknown Course';
      if (weekSchedule.hasOwnProperty(schedule.dayOfWeek)) {
        const startTime = this.convertTo12Hour(schedule.startTime);
        const endTime = this.convertTo12Hour(schedule.endTime);
        const timeRange = `${startTime} - ${endTime}`;
        weekSchedule[schedule.dayOfWeek].push({ timeRange, courseName });
      }
    });
  
    return weekSchedule;
  }
  convertTo12Hour(time: string): string {
    const [hour, minute] = time.split(':').map(Number);
    const suffix = hour >= 12 ? 'PM' : 'AM';
    const adjustedHour = hour % 12 || 12; // Converts "0" hours to "12"
    return `${adjustedHour}:${minute < 10 ? '0' + minute : minute} ${suffix}`;
  }
  updateProgress() {
    this.progressValue += 10;
    if (this.progressValue > 100) {
      this.progressValue = 0; // Reset when reaching 100%
    }
  }
  flipCard() {
    this.isFlipped = !this.isFlipped;
  }

  addCourse() {

  }
}
