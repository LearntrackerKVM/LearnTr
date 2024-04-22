import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-popup-notification',
  standalone: true,
  imports: [],
  templateUrl: './popup-notification.component.html',
  styleUrl: './popup-notification.component.css'
})
export class PopupNotificationComponent {
  @Input() message: string = '';
}
