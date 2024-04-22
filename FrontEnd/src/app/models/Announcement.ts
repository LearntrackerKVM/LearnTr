export interface CourseVisibility {
    courseId: string;
    courseName: string;
}

export interface Announcement {
    id?: string;
    title: string;
    description: string;
    createdBy: string;
    createdById: string;
    priorityName: string;
    priorityId: string;
    visibility: CourseVisibility[];
    createdDate: Date;
    lastModifiedDate: Date;
    attachmentLink: string;
}
