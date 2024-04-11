import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbDatepicker } from '@ng-bootstrap/ng-bootstrap';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import '@angular/localize/init';
import { SchedulerComponent } from '../scheduler/scheduler.component';


interface Habit {
  name: string;
  done: boolean;
}

@Component({
  selector: 'app-my-journal',
  standalone: true,
  imports: [CommonModule,FormsModule,NgbDatepicker,SchedulerComponent],
  templateUrl: './my-journal.component.html',
  styleUrl: './my-journal.component.css'
})
export class MyJournalComponent {
  @ViewChild('journalModal') journalModal: any; 

  selectedDate: any;
  journalEntry: string = '';
  habits: Habit[] = [
    { name: 'Read 30 minutes', done: false },
    { name: 'Exercise', done: false },
    { name: 'Meditate 15 minutes', done: false }
  ];

  constructor(private modalService: NgbModal) {}

  onDateSelect(event: any) {
    this.selectedDate = event;
    this.journalEntry = ''; 
    this.habits.forEach(habit => habit.done = false);
    this.openJournalModal(this.journalModal); // Ensure this line correctly references the modal
  }

  openJournalModal(journalModal: any) {
    this.modalService.open(journalModal, { size: 'lg' });
  }
}