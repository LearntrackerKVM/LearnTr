import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './welcome.component.html',
  styleUrl: './welcome.component.css'
})
export class WelcomeComponent {
  features = [
    {
      title: 'PROGRESS TRACKING',
      description: 'Track your progress and set goals to stay motivated!',
      icon: '/assets/icons/download.jpeg',
      flipped: false,
      color: '#c4e7f7'
  
    },
    {
      title: 'Remainders',
      description: 'Never forget your important tasks with timely reminders.',
      icon: '/assets/icons/remainders.jpeg',
      flipped: false,
      color: '#E6E6FA'
    },
    {
      title: 'LeaderBoards',
      description: 'Compete with others and track your ranking on the leaderboards.',
      icon: '/assets/icons/leaderboards.svg',
      flipped: false,
      color: '#FADADD'
    },
    {
      title: 'Personalized settings',
      description: 'Customize your app settings for a personalized experience.',
      icon: '/assets/icons/images.png',
      flipped: false,
      color: '#98FF98'
    },
    {
      title: 'My Journal',
      description: 'Keep a personal journal of your daily activities and reflections.',
      icon: '/assets/icons/journal.jpeg',
      flipped: false,
      color:'#FFDAB9'
    }
  ];
}
