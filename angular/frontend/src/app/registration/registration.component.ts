import { Component, OnDestroy } from '@angular/core';
import { AsyncPipe, NgIf } from '@angular/common';
import { Subscription, catchError, throwError } from 'rxjs';
import { NgForm } from '@angular/forms';
import { FormsModule } from '@angular/forms';
import { 
  UnauthenticatedHeaderComponent 
} from '../unauthenticated-layout/unauthenticated-header/unauthenticated-header.component';
import { 
  UnauthenticatedFooterComponent 
} from '../unauthenticated-layout/unauthenticated-footer/unauthenticated-footer.component';
import { Observable, of } from 'rxjs';

import { UserRegistrationModel, 
  UserRegistrationResponseModel } from '../models/user-registration.model';
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
export class RegistrationComponent implements OnDestroy{

  constructor(private registrationService: RegistrationService) { }

  isFormPasswordsError = false;

  private registrationSubscription: Subscription | undefined;

  userRegistrationData: UserRegistrationModel = {
    username:'',
    password:'',
    passwordConfirmation:'',
    surname:'',
    givenName:'',
    email: ''

  };

  registrationResponse$: Observable<
    UserRegistrationResponseModel|undefined> = of(undefined);

  passwordErrorMsg:string = 'The passwords must match!'

  apiErrorMsg: string | undefined;

  apiSuccessResponse: UserRegistrationResponseModel | undefined;


  ngOnInit(): void {

  }

  onSubmitRegistrationForm(form: NgForm) {
    console.log('submit resistration ...')
    console.log(form.value);
    if (form.invalid) {
      console.log('registration form is invalid')
      console.log(form.errors);
      return;
    }
    if (form.value.password !== form.value.re_password) {
      console.log('passwords do not match invalid')
      this.isFormPasswordsError = true;
      form.reset();
      return;
    }
    console.log('valid!')
    console.log(form.value.username);
    this.userRegistrationData.username = form.value.username;
    this.userRegistrationData.password = form.value.password;
    this.userRegistrationData.passwordConfirmation = form.value.re_password;
    this.userRegistrationData.email = form.value.contact_email;
    this.userRegistrationData.surname = form.value.surname;
    this.userRegistrationData.givenName = form.value.given_name
    console.log(this.userRegistrationData);
    this.registrationResponse$ = this.registrationService
      .submitUserRegistration(this.userRegistrationData).pipe(
        catchError((error) => {
          let errorMessage = 'An error occurred during registration.';
          if (error.error && error.error.message) {
            errorMessage = error.error.message;
          }
          return throwError(errorMessage);
        })
      );
      this.registrationResponse$.subscribe(
        (response) => {
          this.apiSuccessResponse = response
          form.reset();
        },
        (error) => {
          this.apiErrorMsg = error;
          form.reset();
        }
      );
  }


  onClearRegistrationFormError() {
    this.isFormPasswordsError = false;
  }

  onClearRegistrationMessage() {
    this.registrationResponse$ = of(undefined);
  }

  ngOnDestroy(): void {
    if (this.registrationSubscription) {
      this.registrationSubscription.unsubscribe();
    }
  }

}
