export interface StudentCourseRequest {
  courseId: string;
  courseName: string;
  courseCode: string;
  studentId: string;
  enrolledDate: Date; 
  createdById : string;
  createdBy : string;
  color? : string;
}
