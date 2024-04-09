import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-badges-info',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './badges-info.component.html',
  styleUrl: './badges-info.component.css'
})
export class BadgesInfoComponent {
  badges = [
    { milestone: '0-5', badge: 'Bronze', iconPath: 'assets/badges/bronze.png' },
    { milestone: '5-10', badge: 'Silver', iconPath: 'assets/badges/silver.png' },
    { milestone: '10-15', badge: 'Gold', iconPath: 'assets/badges/gold.png' },
    { milestone: '15-20', badge: 'Platinum', iconPath: 'assets/badges/platinum.png' },
    { milestone: '20-25', badge: 'Diamond', iconPath: 'assets/badges/diamond.png' },
    { milestone: '25-30', badge: 'Ruby', iconPath: 'assets/badges/ruby.png' },
    { milestone: '30-35', badge: 'Sapphire', iconPath: 'assets/badges/sapphire.png' },
    { milestone: '35-40', badge: 'Emerald', iconPath: 'assets/badges/emerald.png' }
  ];
}
