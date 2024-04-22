export class Notebook {
    id?: string;         // Optional for creation
    studentId?: string;
    studentName?: string;
    notes?: string;
    modifiedDate?: Date;  // Making it optional

    constructor(
      studentId?: string,
      studentName?: string,
      notes?: string,
      id?: string,
      modifiedDate?: Date
    ) {
      this.studentId = studentId;
      this.studentName = studentName;
      this.notes = notes;
      this.id = id;
      this.modifiedDate = modifiedDate;
    }
  }
