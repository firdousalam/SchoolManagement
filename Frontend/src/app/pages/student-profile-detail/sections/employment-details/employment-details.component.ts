import { Component, OnInit, Input, EventEmitter, Output, OnChanges, SimpleChanges } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Observable, Subscription } from 'rxjs';
import { NotificationService } from 'src/app/core/services/notification.service';
import { stdProfession } from 'src/app/shared/models/profile';
import { StudentEmploymentServiceService } from 'src/app/shared/services/api/student-employment-service.service';

@Component({
  selector: 'app-employment-details',
  templateUrl: './employment-details.component.html',
  styleUrls: ['./employment-details.component.scss'],
})
export class EmploymentDetailsComponent implements OnInit, OnChanges {
  @Input() studentEmploymentData!: stdProfession;
  @Input() submitEvent!: Observable<void>;
  @Input() studentProfileId: any = '';
  @Output() updatedEmploymentListData = new EventEmitter<any>();
  editMode: boolean = false;
  isValid: boolean = false;
  form!: FormGroup;
  eventsSubscription!: Subscription;
  employmentSubscription!: Subscription;
  formSubscription!: Subscription;
  subscriptionArray: any[] = [];
  constructor(private api: StudentEmploymentServiceService, private formBuilder: FormBuilder, private notificationService: NotificationService) {
    this.form = this.formBuilder.group(
      {
        organization: ['', Validators.required],
        designation: ['', Validators.required],
        placeOfDuty: ['', Validators.required]

      }
    );
  }
  get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }
  onEdit() {
    this.editMode = true;
  }
  onCancel() {
    this.editMode = false;
  }
  ngOnInit(): void {
    this.eventsSubscription = this.submitEvent.subscribe(() => this.sendBackData());
    this.subscriptionArray.push(this.eventsSubscription);
    this.form.patchValue({
      organization: this.studentEmploymentData?.organization,
      designation: this.studentEmploymentData?.designation,
      placeOfDuty: this.studentEmploymentData?.placeOfDuty
    })


  }

  ngOnChanges(changes: SimpleChanges): void {
    this.formSubscription = this.form.valueChanges.subscribe(() => {
      this.isValid = this.form.valid;

    });
    this.subscriptionArray.push(this.formSubscription);
  }

  updateEmploymentData() {
    this.studentEmploymentData = { ...this.studentEmploymentData, 'profileId': parseInt(this.studentProfileId) };
    let requestData: any = { ...this.studentEmploymentData, ...this.form.value };
    delete requestData.status;
    delete requestData.approvalStatus;
    this.employmentSubscription = this.api.updateBySearchCriteria({ profileId: this.studentProfileId }, requestData).subscribe((data: any) => {
      this.notificationService.showSuccessToast(data.message);
      this.editMode = false;
      this.sendBackData();
    })
    this.subscriptionArray.push(this.employmentSubscription);
  }
  sendBackData() {
    this.updatedEmploymentListData.emit(this.form.value);

  }
  ngOnDestroy(): void {
    this.subscriptionArray.forEach((x: any) => x.unsubscribe());
  }
}
