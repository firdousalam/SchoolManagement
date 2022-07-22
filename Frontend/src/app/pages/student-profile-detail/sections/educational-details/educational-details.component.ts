import { Component, OnInit, Input, ViewChild, OnDestroy, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { FormGroup, NgForm } from '@angular/forms';
import { IProfile, IProfilePage, IProfileSearch, stdEducationList } from 'src/app/shared/models/profile'
import { educationDetailsColumnDefs } from 'src/app/shared/constants/admission.constant';
import { Observable, Subscription } from 'rxjs';
import { StudentEducationServiceService } from 'src/app/shared/services/api/student-education-service.service';
import { NotificationService } from 'src/app/core/services/notification.service';


@Component({
  selector: 'app-educational-details',
  templateUrl: './educational-details.component.html',
  styleUrls: ['./educational-details.component.scss'],
})

export class EducationalDetailsComponent implements OnInit, OnDestroy, OnChanges {
  @ViewChild('educationForm', { read: NgForm }) educationForm!: NgForm;
  @Input() studentEducationData!: IProfile;
  studentEducation: any;
  @Input() submitEvent!: Observable<void>;
  @Input() studentProfileId: any = '';
  @Output() updatedEducationListData = new EventEmitter<any>();
  editMode: boolean = false;
  isValid: boolean = false;
  years: any = [];
  eventsSubscription!: Subscription;
  saveEducationSubscription!: Subscription;
  subscriptionArray: any[] = [];
  editCache: { [key: number]: { edit: boolean; data: stdEducationList } } = {};
  listOfData: stdEducationList[] = [];
  updatedEducationData: stdEducationList[] = [];
  i = 0;
  columnDefs: any[];
  constructor(private api: StudentEducationServiceService, private notificationService: NotificationService) {
    this.columnDefs = educationDetailsColumnDefs;
  }
  startEdit(id: number): void {
    console.log('ll', id);

    this.editCache[id].edit = true;
    console.log('ll3', this.editCache);
  }

  cancelEdit(id: number): void {
    const index = this.listOfData.findIndex(item => item.id === id);
    this.editCache[id] = {
      data: { ...this.listOfData[index] },
      edit: false
    };
  }

  saveEdit(id: number): void {
    console.log('dfgd', id);

    const index = this.listOfData.findIndex(item => item.id === id);
    Object.assign(this.listOfData[index], this.editCache[id].data);
    this.editCache[id].edit = false;
    console.log(this.listOfData);

  }

  updateEditCache(): void {
    this.listOfData.forEach(item => {
      this.editCache[item.id] = {
        edit: false,
        data: { ...item }
      };
    });
  }

  isEmptyObject(o: stdEducationList | { [s: string]: unknown; } | ArrayLike<unknown>) {
    console.log(Object.values(o).every(x => x !== null && x !== ''))
    return Object.values(o).every(x => x !== null && x !== '');
  }

  createEduDataAfterActions() {
    console.log('fromsubmit', this.listOfData, this.educationForm);
    if (this.educationForm.invalid) {
      let lastItem: any = this.listOfData.length && this.listOfData[this.listOfData.length - 1].id;
      this.listOfData = this.listOfData.filter(d => d.id !== lastItem);
      let lastItemNew: any = this.listOfData.length && this.listOfData[this.listOfData.length - 1].id;
      this.saveEdit(lastItemNew)
      this.updateEditCache();
      const filterData = this.listOfData.filter(x => this.isEmptyObject(x))
      console.log('fromsubmitAfterFilweeewe', filterData);
      this.updatedEducationData = filterData;
    } else {
      const lastItem: any = this.listOfData.length && this.listOfData[this.listOfData.length - 1].id;
      this.saveEdit(lastItem)
      this.updateEditCache();
      const filterData = this.listOfData.filter(x => this.isEmptyObject(x))
      console.log('fromsubmitAfterFil', filterData);
      this.updatedEducationData = filterData;
    }
    return this.updatedEducationData;
  }

  sendBackData() {
    this.updatedEducationListData.emit(this.createEduDataAfterActions());

  }

  updateEduData() {
    //put api
    const requestData: any[] = this.createEduDataAfterActions();

    console.log('saveeeeee', requestData)
    this.saveEducationSubscription = this.api.updateBySearchCriteriaWithArrayObject({ profileId: this.studentProfileId }, requestData).subscribe((data: any) => {
      this.notificationService.showSuccessToast(data.message);
      this.editMode = false;
      this.sendBackData();
      this.ngOnInit();
    })
    this.subscriptionArray.push(this.saveEducationSubscription);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.studentEducation = this.studentEducationData?.stdEducationList;
  }
  ngOnInit(): void {
    this.eventsSubscription = this.submitEvent.subscribe(() => this.sendBackData());
    this.subscriptionArray.push(this.eventsSubscription);
    for (let i = 1900; i <= 2999; i++) {
      this.years.push(i);
    }
    const emptyData: any = [
      {
        id: 1,
        courseName: '',
        institution: '',
        educationStatus: '',
        percentage: '',
        yearofCompletion: '',
      },
    ];

    // setTimeout(() => {
    this.listOfData = this.studentEducation;
    // this.i = this.listOfData.length + 1;
    this.updateEditCache();
    // }, 200);



  }
  onEdit() {
    this.editMode = true;
  }
  onCancel() {
    this.editMode = false;
    this.ngOnInit();
  }

  addRow(educationForm: NgForm): void {

    if (educationForm.valid) {
      const lastItem: any = this.listOfData.length && this.listOfData[this.listOfData.length - 1].id;
      this.i = parseInt(lastItem) + 1;
      console.log('New I vale', this.i);

      this.saveEdit(this.i - 1)
      this.listOfData = [
        ...this.listOfData,
        {
          id: this.i,
          courseName: '',
          institution: '',
          educationStatus: '',
          percentage: '',
          yearofCompletion: '',
        }
      ];
      this.updateEditCache();
      this.startEdit(this.i);
      this.i = this.listOfData.length + 1;
    } else {
      for (const control of Object.keys(educationForm.controls)) {
        educationForm.controls[control].markAsTouched();
      }
      return;
    }
    console.log(this.listOfData.length, this.i);
    console.log('dsdsd', educationForm.value);

    console.log(this.listOfData, this.i);
  }

  deleteRow(id: number, educationForm: NgForm): void {
    if (educationForm.invalid) {
      const filterData = this.listOfData.filter(x => this.isEmptyObject(x))
      this.listOfData = filterData.filter(d => d.id !== id);
    } else {
      this.listOfData = this.listOfData.filter(d => d.id !== id);
    }

    const lastItem: any = this.listOfData.length && this.listOfData[this.listOfData.length - 1].id;
    this.saveEdit(lastItem)
    this.updateEditCache();

  }
  ngOnDestroy() {
    this.subscriptionArray.forEach(x => x.unsubscribe());
  }
}
