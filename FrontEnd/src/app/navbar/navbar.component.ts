import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { User } from '../models/user';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {

  student: boolean = true;
  isLoggedIn: boolean = false;
  private subscription: Subscription = new Subscription();
  defaultProfilePicture = 'assets/icons/user.png';
  user: any;

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit() {
    this.getUserDetails();
    // Now that subscription is initialized, you can safely call add on it
    this.subscription.add(
      this.userService.currentUser$().subscribe((user) => {
 
        this.isLoggedIn = !!user;
        this.student = user?.role.toLowerCase() === 'student'; // Determine if the user is a student based on the role
      })
    );
    this.subscription = this.userService.isLoggedIn().subscribe(isLoggedIn => {
      this.isLoggedIn = isLoggedIn;
    });
  }

  getUserDetails() {
    const currentUserString = sessionStorage.getItem('currentUser');
    let email = "";
    if (currentUserString) {
      const currentUser = JSON.parse(currentUserString);
      email = currentUser.email;
    }
    this.userService.getUserByEmail(email).subscribe((user) => {
      this.user = user;
    })
  }

  logout() {
    this.userService.logout();
    this.router.navigate(['']);
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }
  navigatetologin() {
    this.router.navigate(["/login"]);
  }
}