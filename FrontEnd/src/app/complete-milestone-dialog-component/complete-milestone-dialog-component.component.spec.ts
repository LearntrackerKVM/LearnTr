import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CompleteMilestoneDialogComponentComponent } from './complete-milestone-dialog-component.component';

describe('CompleteMilestoneDialogComponentComponent', () => {
  let component: CompleteMilestoneDialogComponentComponent;
  let fixture: ComponentFixture<CompleteMilestoneDialogComponentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CompleteMilestoneDialogComponentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CompleteMilestoneDialogComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
