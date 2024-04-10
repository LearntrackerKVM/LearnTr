import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {  FullCalendarModule } from '@fullcalendar/angular';
import { CalendarOptions } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';

@Component({
  selector: 'app-scheduler',
  standalone: true,
  imports: [FullCalendarModule,FormsModule,CommonModule],
  templateUrl: './scheduler.component.html',
  styleUrl: './scheduler.component.css'
})
export class SchedulerComponent {
  selectedDate : any;;
  journalText : string = '';
  showTextarea : boolean = false;
  calendarOptions: CalendarOptions = {
    initialView: 'dayGridMonth',
    plugins: [dayGridPlugin, interactionPlugin],
    dateClick: this.handleDateClick.bind(this), 
    themeSystem: 'bootstrap5',
    eventColor: '#378006', // Sets the background color for events
    eventTextColor: '#ffffff', 
  };

  handleDateClick(arg : any): void {
    this.selectedDate = arg.dateStr; // Store the clicked date
    this.journalText = ''; // Reset/clear previous text
    this.showTextarea = true; // Show the textarea
  }
  saveJournalEntry(): void {
    console.log(`Saving entry for ${this.selectedDate}: ${this.journalText}`);
    // Here, you would typically send the journal entry to your backend or store it locally.
    this.showTextarea = false; // Optionally hide the textarea after saving
  }
  onDateClick(e : any){

  }
}
