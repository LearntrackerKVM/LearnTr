import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-progress-example',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './progress-example.component.html',
  styleUrl: './progress-example.component.css'
})
export class ProgressExampleComponent {
  @Input() progress: number = 0; // The progress value should be between 0 and 100
  gradient!: string;

  constructor() { }

  ngOnInit() {
    this.updateGradient();
  }

  ngOnChanges() {
    this.updateGradient();
  }

  updateGradient() {
    const colorStart = 'white'; // Dark color
    const colorEnd = '#46166b'; // Bright gold/yellow color
    this.gradient = `linear-gradient(to right, ${colorStart} 0%, ${colorEnd} ${this.progress}%)`;
  }
}
