import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-employment-details',
  templateUrl: './employment-details.component.html',
  styleUrls: ['./employment-details.component.scss'],
})
export class EmploymentDetailsComponent implements OnInit {
  editMode: boolean = false;
  constructor() {}
  onEdit() {
    this.editMode = true;
  }
  onCancel(){
    this.editMode = false;
  }
  ngOnInit(): void {}
}
