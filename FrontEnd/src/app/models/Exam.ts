export interface Exam {
    id?: string;
    examNumber: number;
    examName: string;
    courseId: string;
    courseCode: string;
    courseName: string;
    examDate: Date;
    examTime: string;
    roomNumber: string;
    syllabus: string[];
    createdDate: Date;
    createdById: string;
    createdBy: string;
  }
  