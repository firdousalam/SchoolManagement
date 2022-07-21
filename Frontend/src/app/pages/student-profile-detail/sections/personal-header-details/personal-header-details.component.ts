import { Component, OnInit, Input } from '@angular/core';
import {IProfile, IProfilePage, stdPersonalDetail } from 'src/app/shared/models/profile'

@Component({
  selector: 'app-personal-header-details',
  templateUrl: './personal-header-details.component.html',
  styleUrls: ['./personal-header-details.component.scss'],
})
export class PersonalHeaderDetailsComponent implements OnInit {
  @Input() editMode: boolean = false;
  @Input() studentPersonalData!: stdPersonalDetail;

  constructor() {}
 
  ngOnInit(): void {}
}
