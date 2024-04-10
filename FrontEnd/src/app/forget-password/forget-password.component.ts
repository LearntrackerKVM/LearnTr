import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-forget-password',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './forget-password.component.html',
  styleUrl: './forget-password.component.css'
})
export class ForgetPasswordComponent {

email : string = '';
sucess : boolean = false;
failure : boolean = false;
constructor(private userService : UserService){

}
sendResetPasswordLink(email: string) {
  this.userService.sendResetPasswordLink(email).subscribe(
    (response) => {
      // Success: Reset password link sent successfully
      this.sucess = true;
    },
    (error) => {
      // Error: Handle error here
      console.error('Error sending reset password link:', error);
      // Set success to false if an error occurs
      this.failure = true;
      // Additional error handling can be done here if needed
    }
  );
}

}
