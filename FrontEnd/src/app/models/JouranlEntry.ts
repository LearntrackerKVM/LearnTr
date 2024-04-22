import { Habit } from "./Habit";

export class JournalEntry {
  id?: string;
  date: string;
  studentId: string;
  studentName: string;
  morningMood: string;
  nightMood: string;
  habits: Habit[] = [];
  priorities: string[] = [];
  tasks: string[] = [];
  notes: string;
  nightDiary: string;
  importantDay: boolean;
  importantDayDescription: string;
  colorForTheDay: string;

  constructor(init?: Partial<JournalEntry>) {
    this.date = init?.date ?? '';
    this.studentId = init?.studentId ?? '';
    this.studentName = init?.studentName ?? '';
    this.morningMood = init?.morningMood ?? '';
    this.nightMood = init?.nightMood ?? '';
    this.habits = init?.habits ?? [];
    this.priorities = init?.priorities ?? [];
    this.tasks = init?.tasks ?? [];
    this.notes = init?.notes ?? '';
    this.nightDiary = init?.nightDiary ?? '';
    this.importantDay = init?.importantDay ?? false;
    this.importantDayDescription = init?.importantDayDescription ?? '';
    this.colorForTheDay = init?.colorForTheDay ?? '';
  }
}
