import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';
import { AuthenticatedUserRoutingModule } from './authenticated-user-routing.module';

import { UserEffects } from './user/user-state/user.effects';
import { userProfileReducer } from './user/user-state/user.reducers';

import { 
  AuthenticatedFooterComponent 
} from './authenticated-layout/authenticated-footer/authenticated-footer.component';
import { 
  AuthenticatedHeaderComponent 
} from './authenticated-layout/authenticated-header/authenticated-header.component';
import { AuthenticatedUserComponent } from './authenticated-user/authenticated-user.component';
import { UserProfileComponent } from './user/user-profile/user-profile.component';
import { EditProfileFormComponent } from './user/edit-profile-form/edit-profile-form.component';


@NgModule({
  declarations: [
    AuthenticatedFooterComponent,
    AuthenticatedHeaderComponent,
    AuthenticatedUserComponent,
    EditProfileFormComponent,
    UserProfileComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    AuthenticatedUserRoutingModule,
    StoreModule.forFeature('user', userProfileReducer),
    EffectsModule.forFeature([UserEffects]),
  ]
})
export class AuthenticatedUserModule { }
