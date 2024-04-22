import { Component, Input, SimpleChanges } from '@angular/core';

@Component({
  selector: 'app-progress-circle',
  standalone: true,
  imports: [],
  templateUrl: './progress-circle.component.html',
  styleUrl: './progress-circle.component.css'
})
export class ProgressCircleComponent {
  @Input() progress: number = 0;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['progress']) {
      console.log('Progress changed to:', this.progress);
    }
  }

  calculateRotation(): string {
    const rotation = (this.progress * 3.6).toFixed(2);
    return `rotate(${rotation}deg)`;
  }
}