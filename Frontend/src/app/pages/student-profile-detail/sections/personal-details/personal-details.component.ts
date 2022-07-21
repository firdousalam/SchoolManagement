import { Component, OnInit, Input, EventEmitter, Output, OnChanges, SimpleChanges, OnDestroy } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, Subscription } from 'rxjs';
import { IProfile, IProfilePage, IProfileSearch, stdContactDetail } from 'src/app/shared/models/profile';
@Component({
  selector: 'app-personal-details',
  templateUrl: './personal-details.component.html',
  styleUrls: ['./personal-details.component.scss'],
})
export class PersonalDetailsComponent implements OnInit, OnChanges, OnDestroy {
  @Input() studentContactData!: stdContactDetail;
  @Input() submitEvent!: Observable<void>;
  @Input() saveEvent!: Observable<void>;
  @Output() updatedPersonalListData = new EventEmitter<any>();
  @Output() isFormValid = new EventEmitter<any>();
  @Input() editMode: boolean = true;
  eventsSubscription!: Subscription;
  formSubscription!: Subscription;
  saveeventsSubscription!: Subscription;
  subscriptionArray: any[] = [];
  form!: FormGroup;

  constructor(private formBuilder: FormBuilder) {
    this.form = this.formBuilder.group(
      {
        paddress: ['', Validators.required],
        signature: ['', Validators.required],
        caddress: ['', Validators.required],
        mobileNumber: ['', [Validators.required]],
        landlineNumber: ['', [Validators.required]],
        alternateMobileNumber: ['', [Validators.required]],
        emailId: ['', [Validators.required, Validators.email]],

      }
    );
  }
  get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }
  ngOnInit(): void {
    this.eventsSubscription = this.submitEvent.subscribe(() => this.sendBackData());
    this.saveeventsSubscription = this.saveEvent.subscribe(() => this.sendBackData());
    this.subscriptionArray.push(this.saveeventsSubscription)
    this.subscriptionArray.push(this.eventsSubscription);
    this.form.patchValue({
      paddress: this.studentContactData?.paddress,
      signature: this.studentContactData?.signature,
      caddress: this.studentContactData?.caddress,
      mobileNumber: this.studentContactData?.mobileNumber,
      landlineNumber: this.studentContactData?.landlineNumber,
      alternateMobileNumber: this.studentContactData?.alternateMobileNumber,
      emailId: this.studentContactData?.emailId
    })


  }

  ngOnChanges(changes: SimpleChanges): void {

    this.formSubscription = this.form.valueChanges.subscribe(() => {
      this.isFormValid.emit(this.form.valid); 
    });
    this.subscriptionArray.push(this.formSubscription);
  }
  sendBackData() {
    this.updatedPersonalListData.emit(this.form.value);

  }
  ngOnDestroy(): void {
    this.subscriptionArray.forEach((x: any) => x.unsubscribe());
  }
}
