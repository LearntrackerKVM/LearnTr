import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BadgesInfoComponent } from './badges-info.component';

describe('BadgesInfoComponent', () => {
  let component: BadgesInfoComponent;
  let fixture: ComponentFixture<BadgesInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BadgesInfoComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BadgesInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
