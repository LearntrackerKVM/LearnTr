import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FriendRequest } from '../models/FriendRequest';
import { StudentFriends, StudentFriendsRequest } from '../models/StudentFriends';
import { User } from '../models/user';
import { FriendRequestService } from '../services/friend-request.service';
import { StudentFriendsService } from '../services/student-friends.service';
import { UserService } from '../services/user.service';


interface Friend {
  firstName: string;
  lastName: string;
  email: string;
  courses: number;
  studentId : string;
  added: boolean; 
  id: string;
  profilePicture : string;
  accepted : boolean// Added property to track whether the friend is added or not
} 
interface Student {
  firstName: string;
  lastName: string;
  email: string;
  courses: number;
  added: boolean;
  id: string;
  accepted: boolean;
  studentId : string;
  profilePicture :string;
}
@Component({
  selector: 'app-yourfriends',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './yourfriends.component.html',
  styleUrl: './yourfriends.component.css'
})
export class YourfriendsComponent {
  friendsData: Friend[] = [];
  studentsData: Student[] = [];
  createdByID : string = '';
  createdBy : string = '';
  filteredFriends: User[] = [];
  searchFriends: Friend[] = [];
  isSearchVisible = true;
  currentUser : any;
  friendRequests : any[] = [];
  sentRequests = new Set<string>();
  friendsIds = new Set<string>();
  acceptSuccess : boolean = false;
  defaultProfilePicture = 'assets/icons/user.png';

  constructor(private userService : UserService,private studentFriendsService : StudentFriendsService,
    private friendRequestService : FriendRequestService,private router: Router){
    const currentUserString = sessionStorage.getItem('currentUser');
    if (currentUserString) {
       this.currentUser = JSON.parse(currentUserString);
      this.createdByID = this.currentUser.id;
      this.createdBy = this.currentUser.userName;
      
    }
    this.getAllFriends();
    this.loadFriendRequests();
   
  }
  // toggleSearch() {
  //   this.isSearchVisible = !this.isSearchVisible;
  // }

  onSearchChange(event: Event) {  
    const target = event.target as HTMLInputElement;
    const query: string = (target && target.value) || '';
    if (query.trim() === '') {
      this.searchFriends = this.studentsData;
    } else {
      this.searchFriends = this.studentsData.filter(friend => 
        !friend.added &&
        (
          friend.firstName.toLowerCase().includes(query.toLowerCase()) ||
          friend.lastName.toLowerCase().includes(query.toLowerCase()) ||
          friend.email.toLowerCase().includes(query.toLowerCase())
        )
      );
      
    }
  }
  getAllStudents() {
    this.userService.fetchAllStudents().subscribe((students: Student[]) => {
      this.studentsData = students
        .filter(student => student.email !== this.currentUser.email) // Remove current user from list
        .filter(student => !this.friendsIds.has(student.id)) // Remove students who are already friends
        .map(student => ({
          ...student,
          added: false, // Assuming 'added' tracks if a friend request has been sent
          accepted: false // Assuming 'accepted' tracks the status of the friend request
        }));
  
      this.searchFriends = this.studentsData; // Assuming you use this for displaying the filtered list
    });
  }
  
  addFriend(frd: any) {
    const request: StudentFriendsRequest = {
      studentId: frd.id,
      friendId: this.createdByID,
      friendFirstName: frd.firstName,
      friendLastName: frd.lastName,
      friendEmail: frd.email
    };
  
    const friendRequest : FriendRequest = {
      senderId : this.createdByID,
      recipientId : frd.id,
      status : 'pending'

    }
    this.friendRequestService.sendFriendRequest(friendRequest).subscribe(() =>{
      this.sentRequests.add(frd.id); 
    });
  }
  isRequestSent(friendId: string): boolean {
    return this.sentRequests.has(friendId);
  }
  
  getAllFriends() {
    this.studentFriendsService.getFriendsByStudentId(this.createdByID).subscribe(friends => {
      friends.forEach(friend => {
        if (friend.id !== this.createdByID) {
          this.friendsIds.add(friend.id); // Assuming 'id' uniquely identifies a friend
        }
      });
      this.filteredFriends = friends;
      this.getAllStudents(); // Call getAllStudents after fetching friends to ensure synchronization
    });
  }
  acceptFriendRequest(requestId: string) {
    this.friendRequestService.acceptFriendRequest(requestId).subscribe(
      (response) => {
        this.loadFriendRequests();
        this.getAllFriends();
      },
      (error) => {
        console.error('Error accepting friend request:', error);
        // Handle error here if needed
      }
    );
  }
  
  rejectFriendRequest(requestId: any) {
    this.friendRequestService.rejectFriendRequest(requestId).subscribe(
      (response) => {
        this.loadFriendRequests();
        this.getAllFriends();
      },
      (error) => {
        console.error('Error accepting friend request:', error);
        // Handle error here if needed
      }
    );
  }


  loadFriendRequests() {
    this.friendRequestService.getFriendRequests(this.createdByID).subscribe({
      next: (data) => {
        // Filter out accepted requests
        this.friendRequests = data.filter(request => request.status !== 'accepted');
      },
      error: (err) => console.error(err)
    });
  }
  

  checkProgress(friend: any): void {
    this.router.navigate(['/friendsProgress'], { state: { friend: friend } });
  }
}