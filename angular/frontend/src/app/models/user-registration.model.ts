export interface UserRegistrationModel {
    givenName: string | null;
    surname: string | null;
    email: string | null;
    username: string | null;
    password: string | null;
    passwordConfirmation: string | null;
 }

 export interface UserRegistrationResponseModel {
    message: string;
 }