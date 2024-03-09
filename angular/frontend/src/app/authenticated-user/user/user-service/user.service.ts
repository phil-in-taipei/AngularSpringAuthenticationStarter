import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { environment } from '../../../../environments/environment';
import { AuthService } from 'src/app/authentication/auth.service';
import { 
  UserProfileEditModel, 
  UserProfileModel 
} from 'src/app/models/user-profile.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

  fetchUserProfile(): Observable<UserProfileModel> {
    let token = this.authService.getAuthToken();
    return this.http.get<UserProfileModel>(
      `${environment.apiUrl}/api/user/authenticated`,
      { headers: new HttpHeaders(
         { 'Authorization': `Bearer ${token}` }
        ) 
      }
    );
  }

  editUserProfile(
    submissionForm:UserProfileEditModel
    ): Observable<UserProfileModel> {
    let token = this.authService.getAuthToken();
    return this.http.patch<UserProfileModel>(
      `${environment.apiUrl}/api/user/edit`, submissionForm,
      {
        headers: new HttpHeaders({ 'Authorization': `Bearer ${token}` })
      });
  }
}
