import { TestBed } from '@angular/core/testing';

import { StudentFriendsService } from './student-friends.service';

describe('StudentFriendsService', () => {
  let service: StudentFriendsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StudentFriendsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
