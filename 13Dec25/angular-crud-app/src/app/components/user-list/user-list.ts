import { Component,OnInit,signal  } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { UserService  } from '../../services/user';

@Component({
  standalone: true,
  selector: 'app-user-list',
  imports: [CommonModule, RouterLink],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css',
})

export class UserList implements OnInit {
  // users: any[] = [];
  users = signal<any[]>([]);
  loading = signal(true);

  constructor(private userService: UserService ) {}

  ngOnInit() {
    this.userService.getUsers().subscribe(data => {
      console.log('USERS FROM API:', data);
      this.users.set(data);
      this.loading.set(false);
      // this.users = data;
    });
  }

  deleteUser(id: string) {
    this.userService.deleteUser(id).subscribe(() => {
      // this.users = this.users.filter(u => u.id !== id);
      this.users.update(users =>
        users.filter(u => u.id !== id)
      );
    });
  }
}
