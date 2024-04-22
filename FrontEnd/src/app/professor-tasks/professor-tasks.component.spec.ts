import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfessorTasksComponent } from './professor-tasks.component';

describe('ProfessorTasksComponent', () => {
  let component: ProfessorTasksComponent;
  let fixture: ComponentFixture<ProfessorTasksComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfessorTasksComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProfessorTasksComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
