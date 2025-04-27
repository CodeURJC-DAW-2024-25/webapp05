import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginService } from '../services/login.service';

@Injectable()
export class Interceptor implements HttpInterceptor {

  constructor(private loginService: LoginService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Get the token from local storage
    const token = this.getAuthenticationToken();

    // Verify if the request is to the API URL and if the token exists
    if (token) {
      const clonedReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });

      return next.handle(clonedReq);
    }

    // If no token is found, proceed with the original request
    return next.handle(req);
  }

  private getAuthenticationToken() {
    // Replace this with your code to retrieve the auth token from storage
    const token = localStorage.getItem('token');
    return token;
  }
  
}
