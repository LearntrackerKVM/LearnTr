export interface Discussion {
    discussionID: string; // Assuming this is an auto-increment ID from the backend, otherwise use string if it's MongoDB's ObjectID
    parentID: string | null; // Use null for top-level posts that don't have a parent
    courseID: string;
    authorID: string;
    authorName : string;
    contentType: string; // e.g., "Question", "Comment"
    title: string | null; // Can be null for comments
    content: string;
    timestamp: Date; // Consider using string if the date comes in ISO format and converting it to Date object in the frontend if necessary
    attachments: string; // This could be a path or URL
    status: string; // e.g., "Open", "Answered"
    likes: number;
    dislikes: number;
    role: string; // e.g., "Student", "Teacher"
  }