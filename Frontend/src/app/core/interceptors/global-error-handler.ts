import { ErrorHandler, Injectable, Injector } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { GlobalErrorService } from '../services/global-error.service';
import { NotificationService } from '../services/notification.service';

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {
  constructor(private injector: Injector) {}

  handleError(error: Error | HttpErrorResponse) {
    const errorService = this.injector.get(GlobalErrorService);
    const notificationService = this.injector.get(NotificationService);

    if (error instanceof HttpErrorResponse) {
      notificationService.showErrorToast(
        errorService.getServerErrorMessage(error),
        'Server Error'
      );
    } else {
      if (!error?.stack?.includes('NG0100')) {
        notificationService.showErrorToast(
          errorService.getClientErrorMessage(error)
        );
      }
    }
    console.error(error);
  }
}
