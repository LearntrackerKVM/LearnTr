import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfessorhomeComponent } from './professorhome.component';

describe('ProfessorhomeComponent', () => {
  let component: ProfessorhomeComponent;
  let fixture: ComponentFixture<ProfessorhomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfessorhomeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ProfessorhomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
