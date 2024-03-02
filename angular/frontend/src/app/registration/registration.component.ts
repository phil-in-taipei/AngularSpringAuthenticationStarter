import { Component, OnDestroy } from '@angular/core';
import { AsyncPipe, NgIf } from '@angular/common';
import { 
  Subscription, catchError, throwError, 
  Observer, Observable, of 
} from 'rxjs';
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
export class RegistrationComponent implements OnDestroy{

  constructor(private registrationService: RegistrationService) { }

  isFormPasswordsError = false;

  private registrationSubscription: Subscription|undefined;

  userRegistrationData: UserRegistrationModel = {
    username:'',
    password:'',
    passwordConfirmation:'',
    surname:'',
    givenName:'',
    email: ''
  };

  private registrationResponse$: Observable<
    UserRegistrationResponseModel|undefined
    > = of(undefined);

  passwordErrorMsg: string = 'The passwords must match!'

  apiErrorMsg: string|undefined;

  apiSuccessResponse: UserRegistrationResponseModel|undefined;


  ngOnInit(): void {

  }

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

  implementRequestSubscription(): void {
    this.registrationResponse$
      .subscribe({
        next: (response) => {
          this.apiSuccessResponse = response;
        },
        error: (error) => {
          console.log(error)
          this.apiErrorMsg = error;
        }});
  }


  onSubmitRegistrationForm(form: NgForm): void {
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
    this.setUpRequestSubscription();
    this.implementRequestSubscription();
    form.reset();
  }


  onClearRegistrationFormError(): void {
    this.isFormPasswordsError = false;
  }

  onClearRegistrationMessage(): void {
    this.registrationResponse$ = of(undefined);
  }

  ngOnDestroy(): void {
    if (this.registrationSubscription) {
      this.registrationSubscription.unsubscribe();
    }
  }

  setUpRequestSubscription(): void {
    this.registrationResponse$ = this.registrationService
    .submitUserRegistration(this.userRegistrationData).pipe(
      catchError((error) => {
        console.log(error)
        let errorMessage = 'An error occurred during registration.';
        if (error.error && error.error.message) {
          errorMessage = error.error.message;
        }
        return throwError(() => new Error(errorMessage));
      })
    );
  }


}
