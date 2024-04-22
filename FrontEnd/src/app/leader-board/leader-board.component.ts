import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { User } from '../models/user';
import { ProgressExampleComponent } from '../progress-example/progress-example.component';
import { CourseService } from '../services/course.service';
import { LeaderBoardService } from '../services/leader-board.service';
import { StudentTasksService } from '../services/student-tasks.service';
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
  selectedTaskId: string = '';
  filteredStudents : any[] = [];
  uniqueTasks: { taskId: string; title: string }[] = [];
  defaultProfilePicture = 'assets/icons/user.png';

  

  constructor(private leaderboardService: LeaderBoardService,
    private studentTasksService : StudentTasksService, private courseService : CourseService) { 
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
  changeBoard(isleader: any) {
    this.isLeaderBoard = isleader;
  }

  fetchCourseData() {
    if (!this.selectedCourseId) return;
    this.loadTasks();
    this.courseService.getCoursesWithTasksAndMilestonesByStudentId(this.selectedCourseId, this.createdByID).subscribe(data => {
      this.processData(data);
    }, error => {
      console.error('Failed to fetch course progress', error);
    });
  }

  loadTasks(): void {
    if (!this.selectedCourseId) return;
    this.studentTasksService.getAllTasksByStudentIdandCourseId(this.createdByID,this.selectedCourseId,).subscribe(tasks => {
      this.uniqueTasks = tasks;
    });
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
        studentInfo?.tasks.push({
          taskId: task.taskId,
          taskTitle: task.taskTitle,
          difficultyLevel: task.difficultyLevel || 'N/A',
          notes: task.notes || 'No notes',
          milestonesCompletedOutOfTotal: task.milestonesCompletedOutOfTotal,
          taskType: task.taskType,
          status: task.status,
          milestones: task.milestones,
          studentId: task.studentId,
          studentFirstName: task.studentFirstName,
          studentLastName: task.studentLastName,
          studentEmail: task.studentEmail
        });
      });
    });
  
    this.studentTasks = Array.from(studentMap.values());
    // this.extractUniqueTasks();
  }

  // extractUniqueTasks(): void {
  //   const taskSet = new Set<string>();
  //   const tempTasks: { taskId: string; taskName: string }[] = [];

  //   this.studentTasks.forEach(student => {
  //     student.tasks.forEach(task => {
  //       if (!taskSet.has(task.taskId)) {
  //         tempTasks.push({ taskId: task.taskId, taskName: task.taskTitle });
  //         taskSet.add(task.taskId);
  //       }
  //     });
  //   });

  //   this.uniqueTasks = tempTasks;
  // }
  
  populateTable() {
    if (!this.selectedTaskId) {
      // Handle scenario where no specific task is selected, if needed
      return;
    }
    this.filteredStudents = this.studentTasks.map(student => {
      const task = student.tasks.find(t => t.taskId === this.selectedTaskId);
      return { ...student, task };
    }).filter(student => student.task); // Filter out students without the selected task
  }
}
