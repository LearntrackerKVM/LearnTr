import { TestBed } from '@angular/core/testing';

import { TaskMileStonesService } from './task-mile-stones.service';

describe('TaskMileStonesService', () => {
  let service: TaskMileStonesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TaskMileStonesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
