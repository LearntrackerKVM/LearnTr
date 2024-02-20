import { CommonModule } from '@angular/common';
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
import { CircleProgressComponent, NgCircleProgressModule } from 'ng-circle-progress';

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
    HttpClientModule,
    ProgressCircleComponent,
    ProfessorhomeComponent,
    BrowserAnimationsModule,
    NgCircleProgressModule.forRoot({
      // set defaults here
      radius: 100,
      outerStrokeWidth: 16,
      innerStrokeWidth: 8,
      outerStrokeColor: "#78C000",
      innerStrokeColor: "#C7E596",
      animationDuration: 300,
      
    })
  ],
  exports:[ProgressCircleComponent],
  providers: [],
  bootstrap  : [AppComponent]
})
export class AppModule { }
