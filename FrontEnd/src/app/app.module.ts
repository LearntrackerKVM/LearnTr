import { CommonModule, DatePipe } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { ProfessorhomeComponent } from './professorhome/professorhome.component';
import { ProgressCircleComponent } from './progress-circle/progress-circle.component';
import { StudentRegistrationComponent } from './student-registration/student-registration.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgCircleProgressModule } from 'ng-circle-progress';
import { FullCalendarModule } from '@fullcalendar/angular';
import { SchedulerComponent } from './scheduler/scheduler.component';
import { ProgressExampleComponent } from './progress-example/progress-example.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { MatDialogModule } from '@angular/material/dialog';
import { CourseInfoDialogComponent } from './course-info-dialog/course-info-dialog.component';
import { MatButtonModule } from '@angular/material/button';
import { CompleteMilestoneDialogComponentComponent } from './complete-milestone-dialog-component/complete-milestone-dialog-component.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

// Add MatDialogModule to your imports array

@NgModule({
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    CommonModule,
    StudentRegistrationComponent,
    NavbarComponent,
    SchedulerComponent,
    HttpClientModule,
    ProgressCircleComponent,
    ProgressExampleComponent,
    ProfessorhomeComponent,
    BrowserAnimationsModule,
    CourseInfoDialogComponent,
    NgCircleProgressModule.forRoot({
      // set defaults here
      radius: 180,
      outerStrokeWidth: 20,
      innerStrokeWidth: 10,
      outerStrokeColor: "#78C000",
      innerStrokeColor: "#C7E596",
      animationDuration: 300,
      
    }),
    FullCalendarModule,
    MatDialogModule,
    MatButtonModule,
    NgbModule,
    CompleteMilestoneDialogComponentComponent
  ],
  exports:[ProgressCircleComponent,CompleteMilestoneDialogComponentComponent],
  providers: [DatePipe, provideAnimationsAsync()],
  bootstrap  : [AppComponent]
})
export class AppModule { }
