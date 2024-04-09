import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentQuestionsComponent } from './student-questions.component';

describe('StudentQuestionsComponent', () => {
  let component: StudentQuestionsComponent;
  let fixture: ComponentFixture<StudentQuestionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudentQuestionsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(StudentQuestionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
