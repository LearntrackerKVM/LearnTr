import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'LearnTracker';
  showNavbar: boolean = true;
  constructor(private router: Router) {
    router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.showNavbar = !this.router.url.includes('passwordreset'); // Adjust the condition based on your route
      }
    });
  }

  onActivate(event: any) {
    window.scroll(0, 0); // Optionally reset scroll position when route changes
  }
}