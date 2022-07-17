import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-personal-header-details',
  templateUrl: './personal-header-details.component.html',
  styleUrls: ['./personal-header-details.component.scss'],
})
export class PersonalHeaderDetailsComponent implements OnInit {
  @Input() editMode: boolean = false;

  constructor() {}
 
  ngOnInit(): void {}
}
