import { Component } from '@angular/core';
import { 
  UnauthenticatedHeaderComponent 
} from '../unauthenticated-layout/unauthenticated-header/unauthenticated-header.component';
import { 
  UnauthenticatedFooterComponent 
} from '../unauthenticated-layout/unauthenticated-footer/unauthenticated-footer.component';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [
    UnauthenticatedHeaderComponent, 
    UnauthenticatedFooterComponent
  ],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent {

}
