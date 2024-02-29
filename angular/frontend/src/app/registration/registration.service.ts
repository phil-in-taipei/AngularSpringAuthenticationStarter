import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

import { environment } from '../../environments/environment';
import { UserRegistrationModel, 
  UserRegistrationResponseModel } from '../models/user-registration.model';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {


  constructor(private http: HttpClient) { }

  submitUserRegistration(userRegistration: UserRegistrationModel) {
      return this.http.post<UserRegistrationResponseModel>(
        `${environment.apiUrl}/api/register/user`, userRegistration)
  }

}