import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-leader-board',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './leader-board.component.html',
  styleUrl: './leader-board.component.css'
})
export class LeaderBoardComponent {
  leaderboardData = [
    { name: 'Player 1', progress: 60 },
    { name: 'Player 2', progress: 30 },
    { name: 'Player 3', progress: 80 },
    // Add more players as needed
  ];
}
