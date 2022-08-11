import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdmissionPaymentModalComponent } from './admission-payment-modal.component';

describe('AdmissionPaymentModalComponent', () => {
  let component: AdmissionPaymentModalComponent;
  let fixture: ComponentFixture<AdmissionPaymentModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdmissionPaymentModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AdmissionPaymentModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
