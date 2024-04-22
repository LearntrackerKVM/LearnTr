import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FriendsProgressComponent } from './friends-progress.component';

describe('FriendsProgressComponent', () => {
  let component: FriendsProgressComponent;
  let fixture: ComponentFixture<FriendsProgressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FriendsProgressComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FriendsProgressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
