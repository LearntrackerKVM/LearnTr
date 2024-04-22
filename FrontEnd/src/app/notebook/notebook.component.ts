import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { debounceTime } from 'rxjs/operators';
import { Notebook } from '../models/notebook';
import { NotebookService } from '../services/notebook.service';

@Component({
  selector: 'app-notebook',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './notebook.component.html',
  styleUrls: ['./notebook.component.css']
})
export class NotebookComponent implements OnInit {
  notebook: Notebook = new Notebook();
  notebookForm!: FormGroup;
  createdByID: any;
  createdBy: any;
  isSaved = false;
  lastSavedTime: Date | null = null;

  constructor(
    private fb: FormBuilder,
    private notebookService: NotebookService
  ) {
    const currentUserString = sessionStorage.getItem('currentUser');
    if (currentUserString) {
      const currentUser = JSON.parse(currentUserString);
      this.createdByID = currentUser.id;
      this.createdBy = currentUser.userName;
    }
  }

  ngOnInit(): void {
    this.loadInitialNotes();
    this.notebookForm = this.fb.group({
      notes: ''
    });
  }

  loadInitialNotes() {
    this.notebookService.getNotebookBystudentId(this.createdByID).subscribe({
      next: (data) => {
        this.notebook = data;
        this.initializeForm();
        this.setupFormAutoSave();
      },
      error: (err) => {
        console.error('Failed to load notebook', err);
        // Initialize form and setup auto-save even if no data is found
        this.initializeForm();
        this.setupFormAutoSave();
      }
    });
  }

  initializeForm() {
    this.notebookForm = this.fb.group({
      notes: this.notebook.notes || ''
    });
  }

  setupFormAutoSave() {
    this.notebookForm.valueChanges.pipe(
      debounceTime(1000)  // Waits until 1 second of inactivity
    ).subscribe(newValue => {
      this.saveChanges(newValue);
    });
  }

  saveChanges(newValue: any) {
    if (this.notebook.id && newValue.notes.trim()) {
      // Only update if there's an id and actual content
      this.updateNotebook(this.notebook.id, newValue.notes);
    } else if (!this.notebook.id && newValue.notes.trim()) {
      // Create new notebook only if there's actual content to save
      this.createNotebook(newValue.notes);
    }
  }
  

  createNotebook(notes: string) {
    const newNotebook: Notebook = {
      studentId: this.createdByID,
      studentName: this.createdBy,
      notes: notes,
      modifiedDate: new Date()
    };
    this.notebookService.addNotebook(newNotebook).subscribe(
      response => {
        console.log('Created successfully', response);
        this.notebook.id = response.id; // Assuming response includes the new ID
        this.isSaved = true;
        this.lastSavedTime = new Date(); // Update last saved time on successful creation
      },
      error => console.error('Creation failed', error)
    );
  }

  updateNotebook(id: string | undefined, notes: string) {
    if (id) { // Check that id is not undefined
      const updatedNotebook: Notebook = {
        ...this.notebook,
        notes: notes,
        modifiedDate: new Date()
      };
      this.notebookService.updateNotebook(id, updatedNotebook).subscribe(
        response => {
          console.log('Updated successfully', response);
          this.isSaved = true;
          this.lastSavedTime = new Date();
        },
        error => console.error('Update failed', error)
      );
    } else {
      console.log("Error: Notebook ID is undefined.");
    }
  }
  
}
