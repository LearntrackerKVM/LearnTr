export interface Assignment {
  id?: string;
  assignmentName: string;
  assignmentNumber: string;
  courseId: string;
  courseName: string;
  dueDate: Date;
  file: File;
  createdDate: Date;
  createdById: string;
  createdBy: string;
}
