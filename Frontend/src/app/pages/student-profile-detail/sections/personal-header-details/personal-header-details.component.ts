import { Component, OnInit, Input, OnChanges, SimpleChanges, EventEmitter, Output } from '@angular/core';
import { stdPersonalDetail } from 'src/app/shared/models/profile';

@Component({
  selector: 'app-personal-header-details',
  templateUrl: './personal-header-details.component.html',
  styleUrls: ['./personal-header-details.component.scss'],
})
export class PersonalHeaderDetailsComponent implements OnInit, OnChanges {
  @Input() editMode: boolean = false;
  @Input() studentPersonalData!: stdPersonalDetail;
  @Output() employmentStatusChange = new EventEmitter<any>();
  employmentStatus: any;
  constructor() { }

  ngOnChanges(changes: SimpleChanges): void {
    this.employmentStatus = this.studentPersonalData?.employmentStatus;

  }

  radioValueCheck(status: any) {
    this.employmentStatus = status;
    this.employmentStatusChange.emit(this.employmentStatus);
  }
  ngOnInit(): void { }
}
