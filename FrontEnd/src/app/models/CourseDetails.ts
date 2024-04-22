import { CourseSchedule } from "./CourseSchedule";

export interface CourseDetails {
    courseId: string;
    courseName: string;
    courseCode: string;
    availableSlots: number;
    courseSchedules: CourseSchedule[];
  }
