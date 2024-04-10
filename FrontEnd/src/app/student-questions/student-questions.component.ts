import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Course } from '../models/Course';
import { Question } from '../models/Question';
import { CourseService } from '../services/course.service';
import { QuestionService } from '../services/question.service';

@Component({
  selector: 'app-student-questions',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './student-questions.component.html',
  styleUrl: './student-questions.component.css'
})
export class StudentQuestionsComponent {
  allQuestions : Question[] = [];
  question : Question = {
    studentId: '',
    studentName: '', // Changed from 'name' to 'studentName'
    studentEmail: '', // Changed from 'email' to 'studentEmail'
    courseId: '',
    courseName: '',
    professorId: '',
    professorName: '', // Added 'professorName'
    question: '',
    answer: ''
  }
  answerit : any;
  currentUser : any;
  courses : Course[] = [];
  filteredQuestions: any[] = [];

  constructor(private questionService : QuestionService,private courseService : CourseService){
    const currentUserString = sessionStorage.getItem('currentUser');
    if (currentUserString) {
      this.currentUser = JSON.parse(currentUserString);
    }
    this.loadAllCourses();

  }
  getAllQuestions(){
    this.questionService.getAllQuestionsByCourseId(this.question.courseId).subscribe((response) =>{
      this.allQuestions = response;
      this.filteredQuestions = this.allQuestions.map(ques => ({ ...ques, showAnswerField: false }));
    })
  }
  filterCourse(event : any){
    const courseId = event.target?.value;
    const selectedCourse = this.courses.find((course: Course) => course.courseId === courseId);
    
    
    if (selectedCourse) {
      this.question.courseId = selectedCourse.courseId?.toString() ?? ''; 
      this.question.courseName = selectedCourse.courseName;
      this.question.professorId = this.currentUser.id;
      this.question.professorName = this.currentUser.firstName + this.currentUser.lastName;
      this.getAllQuestions();
    } else {
      // Clear question properties if course is not found
      this.question.courseId = '';
      this.question.courseName = '';
      this.question.professorId = '';
      this.question.professorName = '';
    }

  }

  loadAllCourses(): void {
    this.courseService.getCoursesByprofessorId(this.currentUser.id).subscribe(
      (courses: Course[]) => {
        this.courses = courses;
        const courseIds: string[] = courses
          .map(course => course.courseId ? course.courseId.toString() : undefined) // Convert number to string
          .filter((courseId): courseId is string => typeof courseId === 'string'); // Filter out undefined
          
      },
      (error) => {
        console.error('Error loading courses:', error);
      }
    );
  }
  
  postAnswer(question: any) { // Replace `any` with your question type
    if (this.answerit.trim()) {
      this.question.answer = this.answerit;
        this.questionService.postAnswer(question.id,this.answerit).subscribe((reponse)=>{
          this.getAllQuestions();
        })  
      console.log('Posting answer:', question.answer);
    } else {
      alert('The answer field cannot be empty.');
    }
  }
  toggleAnswerField(index: number) {
    this.filteredQuestions[index].showAnswerField = !this.filteredQuestions[index].showAnswerField;
  }
  
}
