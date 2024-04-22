// course.ts

import { CourseSchedule } from './CourseSchedule';

export interface Course {
  courseId?: number; 
  courseName: string;
  courseDescription: string;
  noOfAssignments: number;
  noOfExams: number;
  startDate: Date; 
  endDate: Date;   
  noOfStudentsEnrolled?: number; 
  createdBy: string;
  createdByID: number;
  capacity: number;
  createdAt?: Date; 
  schedules: CourseSchedule[];
  courseCode: '',
  semester: '',
  availableSlots: number;
  color : ''
}
