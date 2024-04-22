// course-schedule.ts

export interface CourseSchedule {
    dayOfWeek: string;
    startTime: string;
    endTime: string;  
    courseId?: number;
    scheduleId? : number
  }