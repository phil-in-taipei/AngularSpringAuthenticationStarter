import { Component } from '@angular/core';
import { AsyncPipe, NgIf } from '@angular/common';
import { first } from 'rxjs';
import { NgForm } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { 
  UnauthenticatedHeaderComponent 
} from '../unauthenticated-layout/unauthenticated-header/unauthenticated-header.component';
import { 
  UnauthenticatedFooterComponent 
} from '../unauthenticated-layout/unauthenticated-footer/unauthenticated-footer.component';

import { UserRegistrationModel, 
  UserRegistrationResponseModel 
} from '../models/user-registration.model';
import { RegistrationService } from './registration.service';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [
    UnauthenticatedHeaderComponent, 
    UnauthenticatedFooterComponent,
    AsyncPipe,
    NgIf,
    FormsModule,
  ],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent {

  constructor(private registrationService: RegistrationService) { }

  userRegistrationData: UserRegistrationModel = {
    username:'',
    password:'',
    passwordConfirmation:'',
    surname:'',
    givenName:'',
    email: ''
  };


  passwordErrorMsg: string|undefined = undefined;

  apiErrorMsg: string|undefined = undefined;

  apiSuccessResponse: UserRegistrationResponseModel|undefined = undefined;

  clearRegistrationData():void {
    this.userRegistrationData = {
      username:'',
      password:'',
      passwordConfirmation:'',
      surname:'',
      givenName:'',
      email: ''
  
    }
  }

  clearTemplateMessages(): void {
    this.passwordErrorMsg = undefined;
    this.apiSuccessResponse = undefined;
    this.apiErrorMsg = undefined;
  }

  implementRegistrationRequest(): void {
    this.registrationService
      .submitUserRegistration(this.userRegistrationData)
        .pipe(first())
        .subscribe({
          next: (response) => {
            this.apiSuccessResponse = response;
          },
          error: (error) => {
            this.apiErrorMsg = 'An error occurred during registration.';
            if (error.error && error.error.message) {
              this.apiErrorMsg = error.error.message;
            }}
        });
  }

  onClearApiError(): void {
    this.apiErrorMsg = undefined;
  }

  onClearApiSuccessResponse(): void {
    this.apiSuccessResponse = undefined;
  }

  onClearFormPasswordError(): void {
    this.passwordErrorMsg = undefined;
  }

  onSubmitRegistrationForm(form: NgForm): void {
    this.clearTemplateMessages();
    if (form.invalid) {
      return;
    }
    if (form.value.password !== form.value.re_password) {
      this.passwordErrorMsg = 'Registration Error. Passwords Must Match!';
      form.reset();
      return;
    }
    this.userRegistrationData.username = form.value.username;
    this.userRegistrationData.password = form.value.password;
    this.userRegistrationData.passwordConfirmation = form.value.re_password;
    this.userRegistrationData.email = form.value.contact_email;
    this.userRegistrationData.surname = form.value.surname;
    this.userRegistrationData.givenName = form.value.given_name
    this.implementRegistrationRequest();
    form.reset();
    this.clearRegistrationData();
  }

}
