import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiscoverInsightsComponent } from './discover-insights.component';

describe('DiscoverInsightsComponent', () => {
  let component: DiscoverInsightsComponent;
  let fixture: ComponentFixture<DiscoverInsightsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DiscoverInsightsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DiscoverInsightsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
