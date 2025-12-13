import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../services/user';

@Component({
  selector: 'app-user-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './user-edit.html',
  styleUrl: './user-edit.css',
})
export class UserEdit implements OnInit {

  id!: number;
  form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private userService: UserService ,
    private router: Router
  ) {}


  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));

    this.form = this.fb.group({
      name: [''],
      email: ['']
    });
    
    this.userService.getUserById(this.id).subscribe(data => {
      this.form.patchValue(data);
    });
  }

  updateUser() {
    this.userService.updateUser(this.id, this.form.value)
      .subscribe(() => {
        alert('User updated');
        this.router.navigate(['/']);
      });
  }
}