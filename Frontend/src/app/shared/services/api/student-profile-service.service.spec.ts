import { TestBed } from '@angular/core/testing';

import { StudentProfileServiceService } from './student-profile-service.service';

describe('StudentProfileServiceService', () => {
  let service: StudentProfileServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StudentProfileServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
