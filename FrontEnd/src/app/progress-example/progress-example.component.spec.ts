import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgressExampleComponent } from './progress-example.component';

describe('ProgressExampleComponent', () => {
  let component: ProgressExampleComponent;
  let fixture: ComponentFixture<ProgressExampleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProgressExampleComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProgressExampleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
