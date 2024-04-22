export interface StudentFriends {
  id: string;
  studentId: string;
  studentName: string;
  friends: Friend[];
}

export interface Friend {
  friendId: string;
  friendFirstName: string;
  friendLastName: string;
  friendEmail: string;
}

export interface StudentFriendsRequest{
  studentId: string;
  friendId: string;
  friendFirstName: string;
  friendLastName: string;
  friendEmail: string;
}