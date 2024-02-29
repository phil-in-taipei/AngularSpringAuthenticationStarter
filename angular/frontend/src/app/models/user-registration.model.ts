export interface UserRegistrationModel {
    givenName: string;
    surname: string;
    username: string;
    password: string;
    passwordConfirmation: string;
 }

 export interface UserRegistrationResponseModel {
    message?: string;
 }