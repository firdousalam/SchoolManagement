import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PersonalHeaderDetailsComponent } from './personal-header-details.component';

describe('PersonalHeaderDetailsComponent', () => {
  let component: PersonalHeaderDetailsComponent;
  let fixture: ComponentFixture<PersonalHeaderDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PersonalHeaderDetailsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PersonalHeaderDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
