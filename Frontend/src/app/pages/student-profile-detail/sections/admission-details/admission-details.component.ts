import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-admission-details',
  templateUrl: './admission-details.component.html',
  styleUrls: ['./admission-details.component.scss'],
})
export class AdmissionDetailsComponent implements OnInit {
  editMode: boolean = false;
  constructor() {}
  ngOnInit(): void {}
  onEdit() {
    this.editMode = true;
  }
  onCancel(){
    this.editMode = false;
  }
}
