import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';


interface Friend {
  name: string;
  email: string;
  courses: number;
  added: boolean; 
  accepted : boolean// Added property to track whether the friend is added or not
} 
@Component({
  selector: 'app-yourfriends',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './yourfriends.component.html',
  styleUrl: './yourfriends.component.css'
})
export class YourfriendsComponent {
  friendsData: Friend[] = [
    { name: 'Alice', email: 'alice@gmail.com', courses: 4, added: false , accepted:false},
    { name: 'Bob', email: 'bob@gmail.com', courses: 3, added: false,accepted:false },
    { name: 'Charlie', email: 'charlie@gmail.com', courses: 2, added: false,accepted:false },
    { name: 'David', email: 'david@gmail.com', courses: 5, added: false,accepted:false },
    { name: 'Eva', email: 'eva@gmail.com', courses: 3, added: false ,accepted:false},
    { name: 'Frank', email: 'frank@gmail.com', courses: 4, added: false ,accepted:false},
    { name: 'Grace', email: 'grace@gmail.com', courses: 2, added: false,accepted:false },
  ];

  filteredFriends: Friend[] = [];
  searchFriends: Friend[] = [];
  isSearchVisible = false;

  toggleSearch() {
    this.isSearchVisible = !this.isSearchVisible;
    if (!this.isSearchVisible) {
      this.searchFriends = [];
    } else {
      this.searchFriends = this.friendsData.filter(friend => friend.added);
    }
  }

  onSearchChange(event: Event) {  
    const target = event.target as HTMLInputElement;
    const query: string = (target && target.value) || '';
    if (query.trim() === '') {
      this.searchFriends = [];
    } else {
      this.searchFriends = this.friendsData.filter(
        friend => !friend.added && friend.name.toLowerCase().includes(query.toLowerCase())
      );
    }
  }

  addFriend(friend: Friend) {
    friend.added = true;
    this.filteredFriends.push(friend);
    this.searchFriends = this.searchFriends.filter(f => f !== friend);
  }
}