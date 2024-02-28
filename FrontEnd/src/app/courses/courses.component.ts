import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Subscription } from 'rxjs';
import { User } from '../models/user';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-courses',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './courses.component.html',
  styleUrl: './courses.component.css'
})
export class CoursesComponent {
  isLoggedIn: boolean = false;
  currentUser: User | null = null;
  courses : any[] = [];
  private subscription: Subscription = new Subscription(); // Initialize the subscription

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    // Subscribe to the isLoggedIn observable to get the login status
    this.subscription.add(this.userService.isLoggedIn().subscribe((loggedInStatus: boolean) => {
      this.isLoggedIn = loggedInStatus;

      // If the user is logged in, retrieve the current user's details
      if (this.isLoggedIn) {
        this.currentUser = this.userService.getCurrentUser();
      }
    }));
  }

  ngOnDestroy(): void {
    // Unsubscribe to prevent memory leaks
    this.subscription.unsubscribe();
  }
}