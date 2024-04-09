import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-progress-circle',
  standalone: true,
  imports: [],
  templateUrl: './progress-circle.component.html',
  styleUrl: './progress-circle.component.css'
})
export class ProgressCircleComponent {
  @Input() progress: number = 0;

  calculateRotation(): string {
    const rotation = (this.progress * 3.6).toFixed(2);
    return `${rotation}deg`;
  }
}