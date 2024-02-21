import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Role } from '../role';
import { Router } from '@angular/router';
import { User } from './../models/user'; 
import { UserService } from '../services/user.service';


@Component({
  selector: 'app-student-registration',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './student-registration.component.html',
  styleUrl: './student-registration.component.css'
})
export class StudentRegistrationComponent {

  user: User = {
    firstName: '',
    lastName: '',
    userName: '',
    email: '',
    password: '',
    role: ''
  };

  password: string = '';
  confirmpassword: string = '';
  hidePassword: boolean = true;
  hideconfirmPassword: boolean = true;
  passwordMatchError: boolean = false;
  constructor(private router: Router,private userService : UserService) {

  }

  roles: Role[] = [
    { id: 1, name: 'Professor' },
    { id: 2, name: 'Student' },
  ];

  selectedRole: number = 0;



  ngOnInit() {
    this.selectedRole = this.roles[0]?.id; // Set the default value here
  }
  ngAfterViewInit(): void {
    const signUpButton = document.getElementById('signUp');
    const signInButton = document.getElementById('signIn');
    const container = document.getElementById('container');

    signUpButton?.addEventListener('click', () => {
      container?.classList.add('right-panel-active');
    });

    signInButton?.addEventListener('click', () => {
      container?.classList.remove('right-panel-active');
    });
  }
  navigateToHomePage() {
    this.router.navigate(['/courses']);
  }
  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }
  toggleConfirmPasswordVisibility(): void {
    this.hideconfirmPassword = !this.hideconfirmPassword;
  }

  checkPasswordMatch(): void {
    this.passwordMatchError = this.password !== this.confirmpassword;
  }

  registerUser(user: User): void {
 
  }
}