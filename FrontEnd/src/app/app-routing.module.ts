import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddNewCourseComponent } from './add-new-course/add-new-course.component';
import { AskteacherComponent } from './askteacher/askteacher.component';
import { BadgesInfoComponent } from './badges-info/badges-info.component';
import { CoursesComponent } from './courses/courses.component';
import { ForgetPasswordComponent } from './forget-password/forget-password.component';
import { FriendsProgressComponent } from './friends-progress/friends-progress.component';
import { LeaderBoardComponent } from './leader-board/leader-board.component';
import { ManageMilestonesComponent } from './manage-milestones/manage-milestones.component';
import { MyJournalComponent } from './my-journal/my-journal.component';
import { PasswordResetComponent } from './password-reset/password-reset.component';
import { ProfessorhomeComponent } from './professorhome/professorhome.component';
import { ProfilePageComponent } from './profile-page/profile-page.component';
import { SchedulerComponent } from './scheduler/scheduler.component';
import { SettingsComponent } from './settings/settings.component';
import { StudentListComponent } from './student-list/student-list.component';
import { StudentQuestionsComponent } from './student-questions/student-questions.component';
import { StudentRegistrationComponent } from './student-registration/student-registration.component';
import { TaskComponent } from './task/task.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { YourfriendsComponent } from './yourfriends/yourfriends.component';

const routes: Routes = [
  {path : '', component:WelcomeComponent},
  { path : 'login', component:StudentRegistrationComponent},
  { path : 'courses', component : CoursesComponent},
  { path : 'profile', component : ProfilePageComponent},
  { path : 'settings',component : SettingsComponent},
  { path : 'leaderboard', component : LeaderBoardComponent},
  { path : 'yourfriends', component : YourfriendsComponent},
  { path : 'professorHome' , component : ProfessorhomeComponent},
  { path : 'askteacher' , component: AskteacherComponent},
  { path : 'myjournal' , component: MyJournalComponent},
  { path : 'schedule' , component: SchedulerComponent},
  { path : 'addaCourse' , component: AddNewCourseComponent},
  { path : 'tasks' , component: TaskComponent},
  { path : 'milestones/:studentTaskId' , component : ManageMilestonesComponent},
  { path : 'forgetpassword', component:ForgetPasswordComponent},
  { path: 'passwordreset', component: PasswordResetComponent },
  { path : 'studentList', component:StudentListComponent},
  { path : 'studentQuestions', component:StudentQuestionsComponent},
  { path : 'badgesinfo' , component : BadgesInfoComponent},
  { path : 'friendsProgress' , component : FriendsProgressComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
