import { TestBed } from '@angular/core/testing';

import { StudyMaterialServiceService } from './study-material-service.service';

describe('StudyMaterialServiceService', () => {
  let service: StudyMaterialServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StudyMaterialServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
