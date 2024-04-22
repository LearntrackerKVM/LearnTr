import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Import FormsModule

@Component({
  selector: 'app-password-reset',
  standalone: true,
  imports: [CommonModule, FormsModule], // Use FormsModule
  templateUrl: './password-reset.component.html',
  styleUrls: ['./password-reset.component.css'] // Correct the property name
})
export class PasswordResetComponent implements OnInit {
  newPassword: string = '';
  confirmPassword: string = '';
  token: string = '';
  showNewPassword: boolean = false;
  showConfirmPassword: boolean = false;
  success : boolean = false;

  // constructor and ngOnInit as before


  constructor(
    private activatedRoute: ActivatedRoute,
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      this.token = params['token'];
    });
  }

  submit() {
    if (this.newPassword === this.confirmPassword && this.token) {
      this.userService.resetPassword(this.token, this.newPassword).subscribe({
        next: (response) => {
          this.success = true;
          this.router.navigate(['/login']);
        },
        error: (error) => {
          this.success = false;
          console.error('Error resetting password:', error);
        }
      });
    }
  }
  // togglePasswordVisibility() {
  //   this.showPassword = !this.showPassword;
  //   let newPasswordInput = document.getElementById('newPassword') as HTMLInputElement;
  //   let confirmPasswordInput = document.getElementById('confirmPassword') as HTMLInputElement;
  //   if (this.showPassword) {
  //     newPasswordInput.type = 'text';
  //     confirmPasswordInput.type = 'text';
  //   } else {
  //     newPasswordInput.type = 'password';
  //     confirmPasswordInput.type = 'password';
  //   }
  // }
}
