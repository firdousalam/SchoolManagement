import { TestBed } from '@angular/core/testing';

import { AdmissionServiceService } from './admission-service.service';

describe('AdmissionServiceService', () => {
  let service: AdmissionServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdmissionServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
