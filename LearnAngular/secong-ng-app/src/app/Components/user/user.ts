import { Component, input, output } from '@angular/core';

@Component({
  selector: 'app-user',
  imports: [],
  templateUrl: './user.html',
  styleUrl: './user.css',
})
export class User {
  users = [
    { id: 0, name: 'abc' },
    { id: 1, name: 'bcd' },
    { id: 2, name: 'pqr' },
    { id: 3, name: 'xyz' },
  ];
  imgUrl =
    'https://plus.unsplash.com/premium_photo-1661877737564-3dfd7282efcb?w=1000&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8aHRtbHxlbnwwfHwwfHx8MA%3D%3D';


  message='';
  showSecretMsg()
  {
    this.message='ha ha ah';
  }
  username = input<string>();

  addItemEvent= output<string>();
  addItem(){

    this.addItemEvent.emit('343341🐢');;
  }
}
