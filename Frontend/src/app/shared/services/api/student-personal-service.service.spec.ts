import { TestBed } from '@angular/core/testing';

import { StudentPersonalServiceService } from './student-personal-service.service';

describe('StudentPersonalServiceService', () => {
  let service: StudentPersonalServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StudentPersonalServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
