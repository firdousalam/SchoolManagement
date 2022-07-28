import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { SpinnerService } from './core/services/spinner.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit,OnDestroy {
  spinner: boolean = false;
  spinnerSubscribe!:Subscription;
  constructor(private spinnerService: SpinnerService) {}

  ngOnInit(): void {
    this.spinnerListener();
  }

  spinnerListener(): void {
   this.spinnerSubscribe =  this.spinnerService.spinnerStatus$.subscribe(spin => {
      this.spinner = spin;
    });
  }

  ngOnDestroy(): void {
    this.spinnerSubscribe.unsubscribe();
  }
}
