export class Habit {
    id?: string;
    name: string;
    completed: boolean;
    studentId?: string;
  
    constructor(name: string) {
        this.name = name;
        this.completed = false;
    }
  }
  