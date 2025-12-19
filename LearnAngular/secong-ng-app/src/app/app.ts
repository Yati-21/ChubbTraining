import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { User } from './Components/user/user';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, User],
  template: `
    <h1>Welcome to {{ title() }}!</h1>
    <app-user username="Yachu" (addItemEvent)="addItem($event)">
      <p>🐢 all the way down {{ items.length }}</p>
    </app-user>
    <a href="/">Home</a>
    |
    <a href="/user">User</a>
    <router-outlet />
  `,
  styles: [],
})
export class App {
  protected readonly title = signal('secong-ng-app');

  items = new Array();
  addItem(item: string) {
    this.items.push(item);
  }
}
