import { ComponentFixture, TestBed } from '@angular/core/testing';

import { YourfriendsComponent } from './yourfriends.component';

describe('YourfriendsComponent', () => {
  let component: YourfriendsComponent;
  let fixture: ComponentFixture<YourfriendsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [YourfriendsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(YourfriendsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
