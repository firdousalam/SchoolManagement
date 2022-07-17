import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

 
@Injectable({
  providedIn: 'root'
})
export class RouteGuard implements CanActivate {
  constructor() {}
  canGoToRoute(user: string, id: string): boolean {
    return true;
  }
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean>|Promise<boolean>|boolean {
    const token = localStorage.getItem('token') || '';
    return this.canGoToRoute(token, route.params.id);
  }
}