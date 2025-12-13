import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService  } from '../../services/user';

@Component({
  standalone: true,
  selector: 'app-user-add',
  imports: [CommonModule, FormsModule],
  templateUrl: './user-add.html',
  styleUrl: './user-add.css',
})
export class UserAdd {
  user = { name: '', email: '' };

  constructor(
    private userService: UserService ,
    private router: Router
  ) {}

  addUser() {
    this.userService.addUser(this.user).subscribe(() => {
      alert('User added');
      this.router.navigate(['/']);
    });
  }
}
