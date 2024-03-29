import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AskteacherComponent } from './askteacher/askteacher.component';
import { CoursesComponent } from './courses/courses.component';
import { LeaderBoardComponent } from './leader-board/leader-board.component';
import { MyJournalComponent } from './my-journal/my-journal.component';
import { ProfessorhomeComponent } from './professorhome/professorhome.component';
import { ProfilePageComponent } from './profile-page/profile-page.component';
import { SettingsComponent } from './settings/settings.component';
import { StudentRegistrationComponent } from './student-registration/student-registration.component';
import { YourfriendsComponent } from './yourfriends/yourfriends.component';

const routes: Routes = [
  { path : '', component:StudentRegistrationComponent},
  { path : 'courses', component : CoursesComponent},
  { path : 'profile', component : ProfilePageComponent},
  { path : 'settings',component : SettingsComponent},
  { path : 'leaderboard', component : LeaderBoardComponent},
  { path : 'yourfriends', component : YourfriendsComponent},
  { path : 'professorHome' , component : ProfessorhomeComponent},
  { path : 'askteacher' , component: AskteacherComponent},
  { path : 'myjournal' , component: MyJournalComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
