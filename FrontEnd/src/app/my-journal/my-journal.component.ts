import { CommonModule } from '@angular/common';
import { Component, OnInit , ChangeDetectorRef, ChangeDetectionStrategy} from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormsModule, FormGroup } from '@angular/forms';
import { NgbDatepicker, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Habit } from '../models/Habit';
import { JournalEntry } from '../models/JouranlEntry';
import { SchedulerComponent } from '../scheduler/scheduler.component';
import { JournalService } from '../services/journal.service';

@Component({
  selector: 'app-my-journal',
  standalone: true,
  imports: [CommonModule,FormsModule, NgbDatepicker, SchedulerComponent, ReactiveFormsModule],
  templateUrl: './my-journal.component.html',
  styleUrls: ['./my-journal.component.css']  
})
export class MyJournalComponent implements OnInit {

  success : boolean = false;
  todayJournal: JournalEntry = new JournalEntry();
  newHabitName: string = '';
  createdByID: any;
  createdBy: any;
  today: Date = new Date();
  journalEntries: JournalEntry[] = [];  // Correct initialization
  constructor(private modalService: NgbModal, private fb: FormBuilder,private cdr: ChangeDetectorRef, private journalService: JournalService) {
    const currentUserString = sessionStorage.getItem('currentUser');
    if (currentUserString) {
      const currentUser = JSON.parse(currentUserString);
      this.createdByID = currentUser.id;
      this.createdBy = currentUser;
    }
    const d = this.today.toLocaleString('en-US', { timeZone: "America/New_York" });
    
    this.todayJournal.priorities = [];
    this.todayJournal.tasks = [];
    this.todayJournal.habits = [];
    this.todayJournal.notes = '';
    this.todayJournal.nightDiary = '';
    this.todayJournal.morningMood = '';
    this.todayJournal.nightMood = '';
  }

  ngOnInit(): void {
    this.addPriority();
    this.addTodo();
    this.initializeTodayJournal();
  }

  addPriority(): void {
    this.todayJournal.priorities.push('');
  }  convertToISO(date: Date): string {
    // Convert date to UTC by subtracting the timezone offset
    const utcDate = new Date(date.getTime() - (date.getTimezoneOffset() * 60000));

    // Return the date in ISO format
    return utcDate.toISOString();
  }
  initializeTodayJournal(): void {
    this.journalService.getJournalEntryByDateAndStudentId(this.today, this.createdByID).subscribe({
      next: (entry) => {
        if (entry) {
          this.todayJournal = entry;
        } else {
          // Set default values if no entry exists
          this.todayJournal = {
            date: this.today.toISOString(),
            studentId: this.createdByID,
            studentName: this.createdBy.userName,
            priorities: [],
            tasks: [],
            habits: [

            ],
            notes: '',
            morningMood: '',
            nightMood: '',
            nightDiary: '',
            importantDay : false,
            colorForTheDay : '',
            importantDayDescription : ''

          };
        }
        console.log(this.todayJournal.date)
      },
      error: (err) => {
        console.error('Error fetching today\'s journal entry:', err);
      }
    });
  
  }
  addTodo(): void {
    this.todayJournal.tasks.push('');
  }


  trackByIndex(index: number, item: any): number {
    return index; // Return the index as the unique identifier
  }
  setMood(mood: string, timeOfDay: 'morning' | 'night'): void {
    if (timeOfDay === 'morning') {
      this.todayJournal.morningMood = mood;
    } else {
      this.todayJournal.nightMood = mood;
    }
  }

  addHabit(): void {
    if (this.newHabitName) {
      const newHabit = new Habit(this.newHabitName);
      this.todayJournal.habits.push(newHabit);
      this.newHabitName = ''; // Clear the input after adding
    }
  }

  toggleHabitCompletion(habit: Habit, index: number): void {
    console.log('Before toggle:', habit.completed); // Debugging output

    habit.completed = !habit.completed;
    // Create a new habit object with the toggled completed property
    const updatedHabit = { ...habit, completed: !habit.completed };

    // Create a new array with the updated habit
    this.todayJournal.habits = [
        ...this.todayJournal.habits.slice(0, index),
        updatedHabit,
        ...this.todayJournal.habits.slice(index + 1)
    ];

    console.log('After toggle:', this.todayJournal.habits[index].completed); // Debugging output
}

  
  

  saveJournalEntry(entry: JournalEntry): void {
    entry.studentId = this.createdByID;
    entry.studentName = this.createdBy.userName;
    if(entry.id !== undefined && entry.id !== null){
    this.journalService.updateJournalEntry(entry.id , entry).subscribe(data => {
      this.todayJournal = data;
      setTimeout(() => {
        this.success = true;
      }, 5000);
    })
    }
    else{
      this.journalService.addJournalEntry(entry).subscribe({
        next: (newEntry) => {
          console.log('Entry saved:', newEntry);
          setTimeout(() => {
            this.success = true;
          }, 5000);
          this.journalEntries.push(newEntry);  // Optionally add to local list
        },
        error: (error) => console.error('Error saving entry:', error)
      });
    }

  }
  toISODateString(date: Date): string {
    const pad = (number: number) => number < 10 ? '0' + number : number.toString();
    return date.getUTCFullYear() + '-' + pad(date.getUTCMonth() + 1) + '-' + pad(date.getUTCDate());
  }
  deleteHabit(index: number): void {
    if (index > -1 && index < this.todayJournal.habits.length) {
      this.todayJournal.habits.splice(index, 1); // Removes the habit at the specified index
    }
  }
  colorChanged(event: any) {
    console.log('New Color:', event.target.value);
    this.todayJournal.colorForTheDay = event.target.value;
  }
}
