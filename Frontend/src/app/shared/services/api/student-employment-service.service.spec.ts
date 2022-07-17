import { TestBed } from '@angular/core/testing';

import { StudentEmploymentServiceService } from './student-employment-service.service';

describe('StudentEmploymentServiceService', () => {
  let service: StudentEmploymentServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StudentEmploymentServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
