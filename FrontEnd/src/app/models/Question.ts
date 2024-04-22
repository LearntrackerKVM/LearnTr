export interface Question {
    studentId: string;
    studentName: string; // Changed from 'name' to 'studentName'
    studentEmail: string; // Changed from 'email' to 'studentEmail'
    courseId: string;
    courseName: string;
    professorId: string;
    professorName: string; // Added 'professorName'
    question: string;
    answer?: string; // Optional field for professor's answer
  }
  