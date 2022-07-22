import { TestBed } from '@angular/core/testing';

import { StudentEducationServiceService } from './student-education-service.service';

describe('StudentEducationServiceService', () => {
  let service: StudentEducationServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StudentEducationServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
