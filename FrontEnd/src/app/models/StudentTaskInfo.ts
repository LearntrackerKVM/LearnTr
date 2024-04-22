export interface TaskDetail {
    taskId: string;
    taskTitle: string;
    difficultyLevel: string | null; // Consider using 'string' if 'null' values are handled elsewhere or providing a default value
    notes: string | null; // Same consideration as above
    milestonesCompletedOutOfTotal: string;
  }
  
  export interface StudentTaskInfo {
    studentfirstName: string;
    studentlastName: string;
    studentid : string;
    studentEmail: string; // Assuming you might need this as per your provided data structure
    tasks: TaskDetail[];
  }
  