import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageMilestonesComponent } from './manage-milestones.component';

describe('ManageMilestonesComponent', () => {
  let component: ManageMilestonesComponent;
  let fixture: ComponentFixture<ManageMilestonesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageMilestonesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ManageMilestonesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
