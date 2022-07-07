import {
  Component,
  OnInit,
  HostBinding,
  OnDestroy,
  Inject,
} from '@angular/core';
import { GlobalDataService } from '../../../core/services/global-data.service';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { NotificationService } from 'src/app/core/services/notification.service';

@Component({
  template: '',
})
export abstract class DetailViewComponent<IDetail>
  implements OnInit, OnDestroy
{
  @HostBinding('class') class = 'page-content-area';
  editMode: boolean = false;
  createMode: boolean = false;
  detail!: IDetail;
  detail$!: Subscription;
  update$!: Subscription;
  create$!: Subscription;

  constructor(
    protected globalDataService: GlobalDataService,
    protected router: Router,
    protected notificationService: NotificationService,
    @Inject('detailObject') protected detailObject: IDetail,
    @Inject('apiService') protected apiService: any,
    @Inject('detailType') protected detailType: string
  ) {
    this.detail = { ...detailObject };
  }

  ngOnInit(): void {
    if (this.router.url.endsWith('new')) {
      this.editMode = true;
      this.createMode = true;
    } else {
      this.editMode = false;
      this.createMode = false;
      this.detail$ = this.globalDataService
        .getAdminDetail()
        .subscribe((data: IDetail) => {
          if (data && Object.keys(data).length === 0) {
            this.notificationService.showErrorToast(
              `${this.detailType} not found!`
            );
          } else {
            this.detail = data;
          }
        });
    }
  }

  onEdit() {
    this.editMode = true;
    this.createMode = false;
  }

  onSave() {
    this.editMode = false;
    this.createMode ? this.createDetail() : this.updateDetail();
  }

  createDetail() {
    this.create$ = this.apiService.create(this.detail).subscribe(() => {
      this.notificationService.showSuccessToast(`${this.detailType} added!`);
      this.onBack();
    });
  }

  updateDetail() {
    this.update$ = this.apiService.update(this.detail).subscribe(() => {
      this.notificationService.showSuccessToast(`${this.detailType} updated!`);
    });
  }

  onBack() {
    this.router.navigateByUrl(`admin-config?tabName=${this.detailType}`);
  }

  ngOnDestroy(): void {
    this.detail$ && this.detail$.unsubscribe();
    this.create$ && this.create$.unsubscribe();
    this.update$ && this.update$.unsubscribe();
  }
}
