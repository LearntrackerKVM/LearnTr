import { CommonModule } from '@angular/common';
import { Component, ɵDeferBlockConfig } from '@angular/core';
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
    role: '',
    id: '',
    aboutMe : '',
    rank: '',
    badge: '',
    milestonesCompleted : 0,
    profilePicture : ''
  };

  registerError : boolean = false;
  registerErrorMessage : string = '';
  password: string = '';
  confirmpassword: string = '';
  hidePassword: boolean = true;
  hideconfirmPassword: boolean = true;
  hideloginPassword :boolean = true;
  passwordMatchError: boolean = false;
  loginError : boolean = false;
  loginErrorMessage : string = '';
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
  navigateToHomePage(email : string,password:string) {

    this.userService.login(email, password).subscribe(
      (user) => {
        if(user.role.toLowerCase() === 'professor'){
          this.router.navigate(['/professorHome']);
        }
        else{
          this.router.navigate(['/courses']);
        }

      },
      (error) => {
        this.loginError = true;
        console.error('Error occurred:', error);
        if (error.status === 404) {
          this.loginErrorMessage = 'Email not found';
        } else if (error.status === 401) {
          this.loginErrorMessage = 'Invalid password';
        } else {
          this.loginErrorMessage = 'An unexpected error occurred';
        }
      }
    );
  }

  
  togglePasswordVisibility(): void {
    this.hidePassword = !this.hidePassword;
  }
  toggleConfirmPasswordVisibility(): void {
    this.hideconfirmPassword = !this.hideconfirmPassword;
  }
  toggleLoginPasswordVisibility(): void{
    this.hideloginPassword = !this.hideloginPassword;
  }

  checkPasswordMatch(): void {
    this.passwordMatchError = this.user.password !== this.confirmpassword;
  }
  registerUser(user: User): void {
    this.userService.registerUser(user).subscribe(
      (userdetails) => {
        this.user.password = '';
        this.user.email = userdetails.email;
        const signInButton = document.getElementById('signIn');
        signInButton?.click();
      },
      (error) => {
        this.registerError = true;
        this.registerErrorMessage = error.error;
      }
    );
    }
  }