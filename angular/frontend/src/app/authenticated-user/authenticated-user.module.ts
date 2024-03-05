import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthenticatedUserComponent } from './authenticated-user/authenticated-user.component';
import { UserProfileComponent } from './user/user-profile/user-profile.component';

import { AuthenticatedUserRoutingModule } from './authenticated-user-routing.module';


@NgModule({
  declarations: [
    AuthenticatedUserComponent, 
    UserProfileComponent],
  imports: [
    CommonModule,
    AuthenticatedUserRoutingModule
  ]
})
export class AuthenticatedUserModule { }
