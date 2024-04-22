import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { Course } from '../models/Course';
import { User } from '../models/user';
import { CourseService } from '../services/course.service';
import { UserService } from '../services/user.service';
import { CourseSchedule } from '../models/CourseSchedule';
import { StudentCourseRequest } from '../models/StudentCourseRequest';
import { ExamService } from '../services/exam.service';
import { AssignmentService } from '../services/assignment.service';
import { StudentTasksService } from '../services/student-tasks.service';
import { TaskMileStonesService } from '../services/task-mile-stones.service';

interface WeekSchedule {
  [day: string]: { timeRange: string, courseName: string }[];
}

@Component({
  selector: 'app-courses',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './courses.component.html',
  styleUrl: './courses.component.css'
})
export class CoursesComponent {
  isLoggedIn: boolean = false;
  currentUser: User | null = null;
  myCourses : any[] = [];
  myFilteredCourses : Course[] = [];
  availableCourses : Course[] = [];
  filteredCourses: Course[] = []; 
  courseswithSchedules: any[] = [];
  MySchedules: any[] =[];
  isCourseInfo : boolean = false;
  CourseInfo : any = [];
  createdBy : string = '';
  createdByID : string = '';
  studentCourseRequest :  StudentCourseRequest = {
    studentId : '',
    courseCode: '',
    courseId: '',
    courseName: '',
    enrolledDate: new Date(),
    createdById : '',
    createdBy : '',

  };
  noOfCoursesEnrolled : number = 0;
   noOfAssignmentsDue  : number = 0;
   noOfExamsDue  : number = 0;
   noOfMilestones : number = 0;
   courseAlreadyExists : boolean = false;
   cards = [
    {
      icon: '/assets/icons/enrolled.svg',
      number: 0,
      title: 'Courses Enrolled',
      color: '#c4e7f7',
      hoverColor: ''
    },
    {
      icon: '/assets/icons/inProgress.svg',
      number: 0,
      title: 'Assignments Due',
      color: '#E6E6FA',
      hoverColor: ''
    },
    {
      icon: '/assets/icons/inProgress.svg',
      number: 0,
      title: 'Exams Due',
      color: '#FADADD',
      hoverColor: ''
    },
    {
      icon: '/assets/icons/completed.svg',
      number: 0,
      title: 'Milestones completed',
      color: '#FFDAB9',
      hoverColor: ''
    }
  ];

  private subscription: Subscription = new Subscription(); // Initialize the subscription
  coursesVisible: boolean = false;

  constructor(private userService: UserService,private courseService : CourseService,private dialog: MatDialog,private examService : ExamService,private assignmentService : AssignmentService,
    private studentTaskService : StudentTasksService,private taskMilestoneService : TaskMileStonesService) { 
    const currentUserString = sessionStorage.getItem('currentUser');
    if (currentUserString) {
      const currentUser = JSON.parse(currentUserString);
      this.createdByID = currentUser.id;
      this.createdBy = currentUser.userName;
    }

    this.getAllCourses();
    this.getStudentCourses();
    this.getDefaultNumbers();
  }

  ngOnInit(): void {
    // Subscribe to the isLoggedIn observable to get the login status
    this.subscription.add(this.userService.isLoggedIn().subscribe((loggedInStatus: boolean) => {
      this.isLoggedIn = loggedInStatus;

      // If the user is logged in, retrieve the current user's details
      if (this.isLoggedIn) {
        this.currentUser = this.userService.getCurrentUser();
      }
    }));
  }

  ngOnDestroy(): void {
    // Unsubscribe to prevent memory leaks
    this.subscription.unsubscribe();
  }

  getAllCourses(){
    this.courseService.getAllCourses().subscribe((courses)=>{
      this.availableCourses = courses;  
      this.filteredCourses = this.availableCourses;
      const courseIds: string[] = courses
          .map(course => course.courseId ? course.courseId.toString() : undefined) // Convert number to string
          .filter((courseId): courseId is string => typeof courseId === 'string'); // Filter out undefined
        this.loadCoursesSchedules(courseIds,courses,false);
      },
      (error) => {
        console.error('Error loading courses:', error);
      }
    );
  }
  
  toggleCoursesVisibility() {
    this.coursesVisible = !this.coursesVisible;  // Toggle visibility
  }
  
  loadCoursesSchedules(courseIds: string[],courses:Course[],ismyschedule : boolean): void {
    this.courseService.getCoursesSchedulesByCourseIds(courseIds).subscribe(
      (schedules: CourseSchedule[]) => {
      if(!ismyschedule){
        this.courseswithSchedules = this.availableCourses.map(course => {
          const filteredSchedules = schedules.filter(schedule => schedule.courseId === course.courseId);
          return {
            ...course,
            weekSchedule: this.transformSchedulesForWeek(filteredSchedules,courses),
          };
        });
      }
      else{
        this.MySchedules = this.availableCourses.map(course => {
          const filteredSchedules = schedules.filter(schedule => schedule.courseId === course.courseId);
          return {
            ...course,
            weekSchedule: this.transformSchedulesForWeek(filteredSchedules,courses),
          };
        });
      }
     

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
  openDialog(course: any): void {
    this.isCourseInfo = true;
    this.CourseInfo = course;
  }
  closeDialog(): void {
    this.isCourseInfo = false;
    this.CourseInfo = null;
  }
  
  searchCourses(searchTerm: string): void {
    if (!searchTerm) {
      this.filteredCourses = this.availableCourses; // Display all courses if the search term is empty
    } else {
      this.filteredCourses = this.availableCourses.filter(course =>
        course.courseName.toLowerCase().includes(searchTerm.toLowerCase()) || 
        course.courseCode.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }
  }
  searchMyCourses(searchTerm: string): void {
    if (!searchTerm) {
      this.myFilteredCourses = this.myCourses; // Display all courses if the search term is empty
    } else {
      this.myFilteredCourses = this.myCourses.filter(course =>
        course.courseName.toLowerCase().includes(searchTerm.toLowerCase()) || 
        course.courseCode.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }
  }
  AddCourse(course : any) {
    this.studentCourseRequest.studentId = this.createdByID;
    this.studentCourseRequest.courseId = course.courseId;
    this.studentCourseRequest.courseCode = course.courseCode;
    this.studentCourseRequest.createdBy = course.createdBy;
    this.studentCourseRequest.createdById = course.createdById;
    this.courseService.addCourseForStudent(this.studentCourseRequest).subscribe((courseDetails) => {
      this.getStudentCourses();
      this.getDefaultNumbers();
    }, (error) => {
      // Handle error here
      this.courseAlreadyExists = true;
        setTimeout(() => {
      this.courseAlreadyExists = false; // Hide the error message after 30 seconds
      this.isCourseInfo = false; // Close the course info
    }, 3000); // 30000 milliseconds = 30 seconds
  
      console.error('Error adding course:', error);
    });
  }
  
  getStudentCourses(){
    this.courseService.getCoursesForStudent(this.createdByID).subscribe((courses)=>{
      this.myCourses = courses;

      const courseIds: string[] = courses
      .map(course => course.courseId ? course.courseId.toString() : undefined) // Convert number to string
      .filter((courseId): courseId is string => typeof courseId === 'string'); // Filter out undefined
      this.myFilteredCourses = this.myCourses;
      if(this.myFilteredCourses !== undefined && this.myFilteredCourses !== null && this.myFilteredCourses.length !== 0)
     this.loadCoursesSchedules(courseIds,this.availableCourses,true);
    })
  }
  getDefaultNumbers(){
    this.courseService.getNumberOfCoursesEnrolledbyStudent(this.createdByID.toString()).subscribe((num)=>{
      this.updateCardNumber('Courses Enrolled', num);
    });
    this.studentTaskService.getNoOfAssignmentsAddedForStudent(this.createdByID.toString()).subscribe(num => {
      this.updateCardNumber('Assignments Due', num);
    });
  
    this.studentTaskService.getNoOfExamsFroStudent(this.createdByID.toString()).subscribe(num => {
      this.updateCardNumber('Exams Due', num);
    });
  
    this.taskMilestoneService.getCompletedMilestonesByStudentid(this.createdByID.toString()).subscribe(num => {
      this.updateCardNumber('Milestones completed', num);
    });
  }

  updateCardNumber(title: string, num: number) {
    const cardIndex = this.cards.findIndex(card => card.title === title);
    if (cardIndex !== -1) {
      this.cards[cardIndex].number = num;
    }
  }
}