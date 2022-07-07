import { Component, OnInit } from '@angular/core';
import { SpinnerService } from './core/services/spinner.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  spinner: boolean = false;

  constructor(private spinnerService: SpinnerService) {}

  ngOnInit(): void {
    this.spinnerListener();
  }

  spinnerListener(): void {
    this.spinnerService.spinnerStatus$.subscribe(spin => {
      this.spinner = spin;
    });
  }
}
