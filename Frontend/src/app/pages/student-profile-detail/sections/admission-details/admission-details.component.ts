import { Component, Input, OnInit } from '@angular/core';
import {stdAdmission } from 'src/app/shared/models/profile'
@Component({
  selector: 'app-admission-details',
  templateUrl: './admission-details.component.html',
  styleUrls: ['./admission-details.component.scss'],
})
export class AdmissionDetailsComponent implements OnInit {
  @Input() studentAdmissionData!:stdAdmission;
  constructor() {}

  ngOnInit(): void {}

}
