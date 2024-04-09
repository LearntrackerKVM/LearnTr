import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AskteacherComponent } from './askteacher.component';

describe('AskteacherComponent', () => {
  let component: AskteacherComponent;
  let fixture: ComponentFixture<AskteacherComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AskteacherComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AskteacherComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
