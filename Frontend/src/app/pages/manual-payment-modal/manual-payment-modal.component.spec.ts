import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManualPaymentModalComponent } from './manual-payment-modal.component';

describe('ManualPaymentModalComponent', () => {
  let component: ManualPaymentModalComponent;
  let fixture: ComponentFixture<ManualPaymentModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ManualPaymentModalComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ManualPaymentModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
