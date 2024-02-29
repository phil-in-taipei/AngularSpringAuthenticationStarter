import { Component } from '@angular/core';
import { 
  UnauthenticatedFooterComponent 
} from '../unauthenticated-layout/unauthenticated-footer/unauthenticated-footer.component';
import { 
  UnauthenticatedHeaderComponent 
} from '../unauthenticated-layout/unauthenticated-header/unauthenticated-header.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    UnauthenticatedFooterComponent,
    UnauthenticatedHeaderComponent
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

}
