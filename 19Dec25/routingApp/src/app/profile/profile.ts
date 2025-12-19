import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-profile',
  imports: [],
  templateUrl: './profile.html',
  styleUrl: './profile.css',
})
export class Profile {
  id:string|null = null;
  constructor(private route:ActivatedRoute)
  {
    this.id= this.route.snapshot.paramMap.get('id');
  }
}
