import { Injectable } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { select, Store } from '@ngrx/store';
import { throwError, of } from 'rxjs';
import { catchError, filter, map, 
  mergeMap, withLatestFrom}  from "rxjs/operators";

import { AppState } from 'src/app/reducers';
import { selectUserProfile } from './user.selectors';
import { UserService } from '../user-service/user.service';
import { 
    UserProfileActionTypes, UserProfileSubmitted, 
    UserProfileSubmissionCancelled, UserProfileLoaded, 
    UserProfileRequested, UserProfileSaved,
} from './user.actions';

