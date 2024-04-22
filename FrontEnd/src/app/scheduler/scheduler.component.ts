


import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef, ElementRef, Renderer2 } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { FullCalendarModule } from '@fullcalendar/angular';
import { CalendarOptions, EventApi, EventClickArg, EventInput } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import interactionPlugin from '@fullcalendar/interaction';
import { JournalEntry } from '../models/JouranlEntry';
import { JournalService } from '../services/journal.service';

@Component({
  selector: 'app-scheduler',
  standalone: true,
  imports: [FullCalendarModule, FormsModule,CommonModule],
  templateUrl: './scheduler.component.html',
  styleUrls: ['./scheduler.component.css']
})
export class SchedulerComponent implements OnInit {
  selectedDate : any = new Date();
  todayJournal: JournalEntry = new JournalEntry();
  noDataMessage: string = 'No data added for this date.';
  isData: boolean = false;
  calendarOptions!: CalendarOptions;
  createdByID: any; // Set this appropriately based on your application logic
  createdBy: any;
  importantDates: string[] = [];
  todayDate : any = new Date();
  lastClickedEvent?: EventApi;
  isFuture : boolean =false;

  constructor(
    private journalService: JournalService,
    private cdr: ChangeDetectorRef,
    private renderer: Renderer2,
    private el: ElementRef
  ) {
    this.selectedDate = new Date(); 
    this.todayDate = new Date();
    this.initializeUser();
  }

  ngOnInit(): void {
    this.journalService.getJournalEntriesByStudentId(this.createdByID).subscribe((data) => {
      // Filter the data to get dates marked as important days
      this.importantDates = data.filter(entry => entry.importantDay).map(entry => entry.date);
      this.calendarOptions.events = this.generateEvents();
    });
    this.calendarOptions = {
      initialView: 'dayGridMonth',
      plugins: [dayGridPlugin, interactionPlugin],
      dateClick: this.handleDateClick.bind(this),
      initialDate: this.selectedDate.toISOString().substring(0, 10), // Ensure the date is in 'YYYY-MM-DD' format
      themeSystem: 'bootstrap',
      events: [], // Define events array here if needed
      eventColor: '#378006', // default color for events
      eventClick: this.handleEventClick.bind(this),
      eventClassNames: this.setEventClassNames.bind(this)
  };
  
  }
  generateEvents(): EventInput[] {
    return this.importantDates.map(date => ({
      title: 'Important Day',
      start: date
    }));
  }
  initializeUser(): void {
    const currentUserString = sessionStorage.getItem('currentUser');
    if (currentUserString) {
      const currentUser = JSON.parse(currentUserString);
      this.createdByID = currentUser.id; // Assuming 'id' is the correct field
      this.createdBy = currentUser.userName;
    }
    this.getInfo();
  }

  handleDateClick(arg: any): void {
    if (this.lastClickedEvent) {
      this.lastClickedEvent.remove(); // Remove the last clicked event
    }
  
    let calendarApi = arg.view.calendar;
    let newEvent = calendarApi.addEvent({
      title: 'Selected Date',
      start: arg.dateStr,
      allDay: true,
      backgroundColor: 'blue', // Highlight color for the selected date
      borderColor: 'blue',
      display: 'background'
    });
  
    this.lastClickedEvent = newEvent;
    this.selectedDate = arg.dateStr;
    this.getInfo();
    this.calendarOptions.initialDate = this.selectedDate.substring(0, 10); // Update initialDate option
    this.cdr.detectChanges();
    const formattedTodayDate = [
      this.todayDate.getFullYear(), // Get the full year (2024)
      ('0' + (this.todayDate.getMonth() + 1)).slice(-2), // Get the month (April is 3; +1 makes it 04), pad with zero if necessary
      ('0' + this.todayDate.getDate()).slice(-2) // Get the day (21), pad with zero if necessary
  ].join('-');
  
  if (this.selectedDate > formattedTodayDate) {
    this.isFuture = true;
    this.todayJournal.date = this.selectedDate;
    this.addPriority();
    this.addTask();
} else if (this.selectedDate < formattedTodayDate) {
  this.isFuture = false;
} else {
  this.isFuture = false;
}
}
addPriority(): void {
  this.todayJournal.priorities.push('');
}

addTask(): void {
  this.todayJournal.tasks.push('');
}
saveJournalEntry(): void {
  this.todayJournal.studentId = this.createdByID;
  this.todayJournal.studentName = this.createdBy;
  this.todayJournal.date = this.selectedDate;
  if (this.todayJournal.id) {
      this.journalService.updateJournalEntry(this.todayJournal.id, this.todayJournal).subscribe({
          next: data => console.log('Journal updated', data),
          error: error => console.error('Failed to update journal', error)
      });
  } else {
      this.journalService.addJournalEntry(this.todayJournal).subscribe({
          next: data => {
              console.log('Journal added', data);
              this.todayJournal.id = data.id;  // Assuming the response includes the new ID
          },
          error: error => console.error('Failed to add journal', error)
      });
  }
}

trackByFn(index: any, item: any) {
  return index; // or item.id if items have unique ids
}

handleEventClick(clickInfo: EventClickArg): void {
  if (clickInfo.event === this.lastClickedEvent) {
    clickInfo.event.remove();
    this.lastClickedEvent = undefined;
  }
}

setEventClassNames(args: { event: EventApi; isMirror: boolean; isStart: boolean; isEnd: boolean; isSelected: boolean; }): string[] {
  if (args.event === this.lastClickedEvent) {
    return ['selected-date']; // Apply custom class
  }
  return [];
}
  getInfo(): void {
    this.journalService.getJournalEntryByDateAndStudentId(this.selectedDate, this.createdByID)
      .subscribe((data) => {
        if (data && Object.keys(data).length !== 0) {
          this.todayJournal = data;
          this.isData = true;
          this.noDataMessage = '';
          this.changeJournalContainerBackgroundColor(this.todayJournal.colorForTheDay);
        } else {
          this.resetJournalData();
          this.noDataMessage = 'No data added for this date.';
        }
      }, (error) => {
        this.resetJournalData();
        this.noDataMessage = 'Failed to fetch data. Please try again later.';
      });
  }
  changeJournalContainerBackgroundColor(color: string): void {
    this.renderer.setStyle(this.el.nativeElement.querySelector('body'), 'background-color', color);
  }
  
  resetJournalData(): void {
    this.todayJournal = new JournalEntry(); // Reset or recreate your JournalEntry structure as needed
    this.isData = false;
  }
}
