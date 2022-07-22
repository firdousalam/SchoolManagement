import { TestBed } from '@angular/core/testing';

import { ContactQueryServiceService } from './contact-query-service.service';

describe('ContactQueryServiceService', () => {
  let service: ContactQueryServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ContactQueryServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
