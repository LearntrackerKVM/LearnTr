import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Announcement } from '../models/Announcement';
import { Discussion } from '../models/Discussion';
import { Question } from '../models/Question';
import { AnnouncementService } from '../services/announcement.service';
import { CourseService } from '../services/course.service';
import { DiscussionService } from '../services/discussion.service';
import { QuestionService } from '../services/question.service';

@Component({
  selector: 'app-askteacher',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './askteacher.component.html',
  styleUrl: './askteacher.component.css'
})
export class AskteacherComponent {

  isDiscussion: boolean = true;
  studentCourses: any = [];
  posts: any = [];
  showPostForm = false;
  newPostContent = '';
  newPostTitle = '';
  replyContent = '';
  showReplyForm: any = null;
  currentUser: any;
  discussion: Discussion = {
    discussionID: '',
    parentID: null,
    courseID: '',
    authorID: '',
    authorName: '',
    contentType: '',
    title: null,
    content: '',
    timestamp: new Date(),
    attachments: '',
    status: '',
    likes: 0,
    dislikes: 0,
    role: 'student'
  };
  showannoucemnet : boolean = false;
  announcements : Announcement[] = [];
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
  questionsent : boolean = false;
  allQuestions : Question[] = [];

  
  currentReplyTarget: any = null;
  constructor(private discussionService: DiscussionService, private coursesService: CourseService,private announcementService : AnnouncementService
    ,private questionService : QuestionService) {
    const currentUserString = sessionStorage.getItem('currentUser');
    if (currentUserString) {
      this.currentUser = JSON.parse(currentUserString);
      this.discussion.authorID = this.currentUser.id;

    }
  }

  ngOnInit(): void {
    this.loadStudentCourses();
    this.filterDiscussions(this.discussion.courseID);
    this.getAllAnnouncements(this.discussion.courseID);

  }

  getAllAnnouncements(id : any) {
    this.announcementService.getAnnouncementsByCourseId(id)
      .subscribe(
        (data: Announcement[]) => {
          this.announcements = data;
        },
        error => {
          console.error('Error fetching announcements: ', error);
          // Handle error as needed
        }
      );
  }
  replyToReply(post: any, reply: any): void {

    this.discussion.parentID = reply.id; 
    this.discussion.authorID = this.currentUser.id;
    this.discussion.content = this.replyContent;
    this.discussion.authorName = this.currentUser.userName;
    this.discussion.courseID = post.courseID;
    
    this.discussionService.postDiscussion(this.discussion).subscribe({
      next: (newReply) => {
        this.getDiscussionsbyCourse(this.discussion.courseID);
      
        this.currentReplyTarget = null;
        this.replyContent = '';
      },
      error: (error) => {
        console.error('Error creating reply:', error);
      }
    });
  }

  changeBoard(isDiscuss: any) {
    this.isDiscussion = isDiscuss;
  }
   organizeReplies(posts: any[]): any[] {
    // First, ensure all posts are initialized with an empty replies array
    posts.forEach(post => post.replies = []);
  
    const repliesMap = new Map<string, any[]>();
  
    // Assume replies are part of the same array or merged into it
    posts.forEach(item => {
      if (!item.parentID) {
        // Initialize top-level posts with an empty array in the map
        repliesMap.set(item.id, item.replies);
      } else {
        // Attempt to find and push the item into its parent's replies array
        const parentReplies = repliesMap.get(item.parentID);
        if (parentReplies) {
          parentReplies.push(item);
        } else {
          // This block handles orphaned replies or initializes arrays for new parents
          // Depending on your data's consistency, you may handle this differently
          repliesMap.set(item.parentID, [item]);
        }
      }
    });
  
    // Filter to return only top-level posts; assumes replies are not in the initial posts array
    // or have been correctly associated to their parent posts
    return posts.filter(post => !post.parentID);
  }
  

  filterDiscussions(event: any) {

    const courseID = event.target?.value;
    this.getDiscussionsbyCourse(courseID);
    this.showannoucemnet = true;
    this.getAllAnnouncements(courseID);
  }
  filterCourse(event: any) {
    const courseId = event.target?.value;
    const selectedCourse = this.studentCourses.find((course: { courseId: any; }) => course.courseId === courseId);
    if (selectedCourse) {
      this.question.courseId = selectedCourse.courseId;
      this.question.courseName = selectedCourse.courseName;
      this.question.professorId = selectedCourse.professorId;
      this.question.professorName = selectedCourse.professorName;
    } else {
      // Clear question properties if course is not found
      this.question.courseId = '';
      this.question.courseName = '';
      this.question.professorId = '';
      this.question.professorName = '';
    }
    this.getAllQuestions();
  }
  createPost(): void {
    this.discussion.authorName = this.currentUser.userName;
    this.discussionService.postDiscussion(this.discussion).subscribe({
      next: (post) => {
        this.getDiscussionsbyCourse(this.discussion.courseID);
        this.discussion = {
          discussionID: '',
          parentID: null,
          courseID: post.courseID,
          authorID: '',
          authorName: '',
          contentType: '',
          title: null,
          content: '',
          timestamp: new Date(),
          attachments: '',
          status: '',
          likes: 0,
          dislikes: 0,
          role: 'student'
        };
        this.showPostForm = false;
      },
      error: (error) => {
        console.error('Error creating post:', error);
      }
    });
  
  }

  replyToPost(post: any): void {
    this.discussion.parentID = post.id;
    this.discussion.authorID = this.currentUser.id;
    this.discussion.authorName = this.currentUser.userName;
    this.discussion.content = this.replyContent;
    this.discussion.courseID = post.courseID; 
    this.discussionService.postDiscussion(this.discussion).subscribe({
      next: (savedReply) => {
        // Find the post in the posts array and add the reply to its replies array
        const postIndex = this.posts.findIndex((p: any) => p.id === post.id);
        if (postIndex !== -1) {
          if (!this.posts[postIndex].replies) {
            this.posts[postIndex].replies = [];
          }
          this.posts[postIndex].replies.push(savedReply);
        }
  
        // Resetting the form and closing it
        this.resetReplyForm(post.courseID);
      },
      error: (error) => {
        console.error('Error creating post:', error);
      }
    });
  }
  
  resetReplyForm(courseID: string) {
    this.showReplyForm = null; // Hide reply form
    this.replyContent = ''; // Reset reply content
    // Optionally refresh discussions or just the replies for the current post
     this.getDiscussionsbyCourse(courseID); // Uncomment if you wish to refresh all posts and replies
  }
  showReplyFormForReply(replyId: any) {
    this.currentReplyTarget = replyId;
    // Reset or initialize other needed properties, such as replyContent
    this.replyContent = '';
  }

  likePost(postId: string): void {
    this.discussionService.likePost(postId).subscribe((response) => {
      // Assuming response contains the updated likes count and the post ID
      const index = this.posts.findIndex((post: { id: string; }) => post.id === postId);
      if (index !== -1) {
        this.posts[index].likes = response.likes; // Update likes count for the post
      }
    });
  }

  dislikePost(postId: string): void {
    this.discussionService.dislikePost(postId).subscribe((response) => {
      // Assuming response contains the updated likes count and the post ID
      const index = this.posts.findIndex((post: { id: string; }) => post.id === postId);
      if (index !== -1) {
        this.posts[index].dislikes = response.dislikes; // Update likes count for the post
      }
    });
  }
  likeReply(postId: string) {
    this.discussionService.likePost(postId).subscribe((response: { likes: number; postId: string; }) => {
      // Assuming response contains the updated likes count and the post ID
      const index = this.posts.replies.findIndex((post: { id: string; }) => post.id === response.postId);
      if (index !== -1) {
        this.posts.replies[index].likes = response.likes; // Update likes count for the post
      }
    });
  }
  
  dislikeReply(postId: string) {
    this.discussionService.dislikePost(postId).subscribe((response) => {
      // Assuming response contains the updated likes count and the post ID
      const index = this.posts.findIndex((post: { id: string; }) => post.id === postId);
      if (index !== -1) {
        this.posts[index].dislikes = response.dislikes; // Update likes count for the post
      }
    });
  }

  loadStudentCourses() {
    this.coursesService.getCoursesForStudent(this.currentUser.id).subscribe((response) => {
      this.studentCourses = response;
    })
  }
  getDiscussionsbyCourse(courseID: any) {
    this.discussionService.getAllDiscussionsByCourseID(courseID).subscribe((response) => {
      
      // Function to recursively find and assign replies to a post
      const assignReplies = (post: { id: any; replies: any[]; }) => {
        const replies = response.filter(reply => reply.parentID === post.id);
        post.replies = replies;
        
        // For each reply, recursively assign its replies
        post.replies.forEach(assignReplies);
      };
      
      // Filter top-level posts and assign replies recursively
      const postsWithNestedReplies = response.filter(post => post.parentID == null);
      postsWithNestedReplies.forEach(assignReplies);
      
      // Update component state
      this.posts = postsWithNestedReplies;
    });
  }

  getAllQuestions(){
    this.questionService.getAllQuestionsByCourseId(this.question.courseId).subscribe((response) =>{
      this.allQuestions = response;
    })
  }
  postQuestion(){
    this.question.studentId = this.currentUser.id;
    this.question.studentEmail = this.currentUser.email;
    this.question.studentName = this.currentUser.firstName + this.currentUser.lastName;
    this.questionService.postQuestion(this.question).subscribe(
      (response) => {
        setTimeout(() => {
          this.questionsent = true;
        }, 3000); // 30000 milliseconds = 30 seconds
      this.getAllQuestions();
        console.log('Question posted successfully:', response);

      },
      (error) => {
        console.error('Error posting question:', error);
      }
    );
  }
  
}