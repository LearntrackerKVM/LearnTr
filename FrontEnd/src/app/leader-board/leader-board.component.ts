import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { User } from '../models/user';
import { ProgressExampleComponent } from '../progress-example/progress-example.component';
import { CourseService } from '../services/course.service';
import { LeaderBoardService } from '../services/leader-board.service';
interface Milestone {
  id: string;
  title: string;
  difficultyLevel: string | null;
  notes: string | null;
  status: string;
}

interface Task {
  taskId: string;
  taskTitle: string;
  taskType: string; // Missing in your object
  difficultyLevel: string | null;
  notes: string | null;
  status: string; // Missing in your object
  milestones: Milestone[];
  milestonesCompletedOutOfTotal: string;
  studentId: string;
  studentFirstName: string;
  studentLastName: string;
  studentEmail: string;
}

interface Course {
  id: string;
  name: string;
  code: string;
  tasks: Task[];
}

interface StudentTaskInfo {
  studentfirstName: string;
  studentlastName: string;
  studentid: string;
  studentEmail: string;
  tasks: Task[];
}

@Component({
  selector: 'app-leader-board',
  standalone: true,
  imports: [CommonModule,FormsModule,ProgressExampleComponent],
  templateUrl: './leader-board.component.html',
  styleUrl: './leader-board.component.css'
})

export class LeaderBoardComponent implements OnInit {
  leaderboardData: User[] = [];
  selectedFilter: string = 'allStudents';
  ascendingOrder: boolean = true;
  createdByID : any;
  createdBy : any;
  myCourses : any[] = [];
  selectedCourseId : any = '';
  isLeaderBoard : boolean = true;
  selectedCourseData: any = null;
  public studentTasks: StudentTaskInfo[] = [];
  public maxTasks: number = 0;
  

  constructor(private leaderboardService: LeaderBoardService, private courseService : CourseService) { 
    const currentUserString = sessionStorage.getItem('currentUser');
    if (currentUserString) {
      const currentUser = JSON.parse(currentUserString);
      this.createdByID = currentUser.id;
      this.createdBy = currentUser.userName;
    }

  }

  ngOnInit(): void {
    this.loadLeaderboardData();
    this.getStudentCourses();
  }
  getStudentCourses() {
    this.courseService.getCoursesForStudent(this.createdByID).subscribe((courses) => {
      this.myCourses = courses;
    });
  }

  loadLeaderboardData(): void {
    switch (this.selectedFilter) {
      case 'allStudents':
        this.leaderboardService.getAllStudentsByRanking().subscribe(data => {
          this.leaderboardData = data;
        });
        break;
      case 'studentsWithCourses':
        // Assuming you have this.createdByID available here
        this.leaderboardService.getStudentsWithSameCourses(this.createdByID).subscribe(data => {
          this.leaderboardData = data;
        });
        break;
      case 'yourFriends':
        // Assuming you have this.createdByID available here
        this.leaderboardService.getStudentFriends(this.createdByID).subscribe(data => {
          this.leaderboardData = data;
        });
        break;
      case 'friendsWithCourses':
        // Assuming you have this.createdByID available here
        this.leaderboardService.getFriendsWithSameCourses(this.createdByID).subscribe(data => {
          this.leaderboardData = data;
        });
        break;
    }
  }

  filterData(): void {
    this.loadLeaderboardData();
  }

  sortData(field: keyof User): void {
    this.leaderboardData.sort((a, b) => {
      let valA = a[field];
      let valB = b[field];
  
      // Check if the values are numeric and compare as numbers
      if (typeof valA === 'number' && typeof valB === 'number') {
        return this.ascendingOrder ? valA - valB : valB - valA;
      }
  
      // Otherwise, treat as strings
      valA = String(valA).toUpperCase(); // Ensure the value is treated as a string
      valB = String(valB).toUpperCase();
  
      if (valA < valB) {
        return this.ascendingOrder ? -1 : 1;
      } else if (valA > valB) {
        return this.ascendingOrder ? 1 : -1;
      }
      return 0;
    });
    this.ascendingOrder = !this.ascendingOrder;
  }
  
  
  getBadgeImagePath(badge: string): string | null {
    const badgeLower = badge.toLowerCase();
    const badges: Record<string, string> = {
      bronze: 'assets/badges/bronze.png',
      silver: 'assets/badges/silver.png',
      gold: 'assets/badges/gold.png',
      platinum: 'assets/badges/platinum.png',
      diamond: 'assets/badges/diamond.png',
      ruby: 'assets/badges/ruby.png',
      sapphire: 'assets/badges/sapphire.png',
      emerald: 'assets/badges/emerald.png',
    };
  
    return badges[badgeLower] || null;
  }
  

  checkProgress() {
    this.courseService.getCoursesWithTasksAndMilestonesByStudentId(this.selectedCourseId, this.createdByID).subscribe(data => {
      this.selectedCourseData = data;
      this.processData(this.selectedCourseData);
    }, error => {
      console.error('Failed to fetch course progress', error);
      this.selectedCourseData = null; 
    });
  }
  changeBoard(isleader: any) {
    this.isLeaderBoard = isleader;
  }

  processData(data: Course[]): void {
    const studentMap = new Map<string, StudentTaskInfo>();
  
    data.forEach((course: Course) => {
      course.tasks.forEach((task: Task) => {
        const studentKey = `${task.studentId}`;
        if (!studentMap.has(studentKey)) {
          studentMap.set(studentKey, {
            studentfirstName: task.studentFirstName,
            studentlastName: task.studentLastName,
            studentEmail: task.studentEmail,
            studentid: task.studentId,
            tasks: []
          });
        }
  
        const studentInfo = studentMap.get(studentKey);
        if (studentInfo) {
          studentInfo.tasks.push({
            taskId: task.taskId,
            taskTitle: task.taskTitle,
            difficultyLevel: task.difficultyLevel || 'N/A',
            notes: task.notes || 'No notes',
            milestonesCompletedOutOfTotal: task.milestonesCompletedOutOfTotal,
            taskType: task.taskType, // Ensure this data is present in the incoming task object
            status: task.status, // Ensure this data is present in the incoming task object
            milestones: task.milestones, // Assuming this matches the Milestone[] type
            // Additional properties not originally in Task but seems to be included now
            studentId: task.studentId,
            studentFirstName: task.studentFirstName,
            studentLastName: task.studentLastName,
            studentEmail: task.studentEmail,
          });
        }
      });
    });
  
    this.studentTasks = Array.from(studentMap.values());
    this.maxTasks = Math.max(...this.studentTasks.map(student => student.tasks.length));
  }
  
}