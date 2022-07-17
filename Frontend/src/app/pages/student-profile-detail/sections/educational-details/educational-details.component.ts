import { Component, OnInit, Input, ViewChild, OnDestroy, Output, EventEmitter } from '@angular/core';
import { FormGroup, NgForm } from '@angular/forms';
import { IProfile, IProfilePage, IProfileSearch, stdEducationList } from 'src/app/shared/models/profile'
import { educationDetailsColumnDefs } from 'src/app/shared/constants/admission.constant';
import { Observable, Subscription } from 'rxjs';
interface ItemData {
  id: number;
  dateofEntry?: string;
  sequence?: number | null;
  courseName: string;
  institution: string;
  educationStatus: string;
  percentage: number | null;
  yearofCompletion: number | null;
}

@Component({
  selector: 'app-educational-details',
  templateUrl: './educational-details.component.html',
  styleUrls: ['./educational-details.component.scss'],
})

export class EducationalDetailsComponent implements OnInit, OnDestroy {
  @ViewChild('educationForm', { read: NgForm }) educationForm!: NgForm;
  @Input() studentEducationData: stdEducationList[] = [];
  @Input() submitEvent!: Observable<void>;
  @Output() updatedEducationListData = new EventEmitter<any>();
  editMode: boolean = false;
  eventsSubscription!: Subscription;
  subscriptionArray: any[] = [];
  editCache: { [key: string]: { edit: boolean; data: ItemData } } = {};
  listOfData: ItemData[] = [];
  updatedEducationData: ItemData[] = [];
  i = 0;
  columnDefs: any[];
  constructor() {
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
  isEmptyObject(o: ItemData | { [s: string]: unknown; } | ArrayLike<unknown>) {
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
    console.log('saveeeeee', this.createEduDataAfterActions())
  }
  ngOnInit(): void {
    this.eventsSubscription = this.submitEvent.subscribe(() => this.sendBackData());
    this.subscriptionArray.push(this.eventsSubscription);
    const data: any = [
      {
        id: 1,
        courseName: 'Course 1',
        institution: 'Institution 1',
        educationStatus: 'Ongoing',
        percentage: 99,
        yearofCompletion: 2022,
      },
    ];

    this.listOfData = data;
    // this.i = this.listOfData.length + 1;
    this.updateEditCache();
  }
  onEdit() {
    this.editMode = true;
  }
  onCancel() {
    this.editMode = false;
    this.ngOnInit();
  }
  onSubmit(educationForm: NgForm) {
    console.log(educationForm);

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
          percentage: null,
          yearofCompletion: null,
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
