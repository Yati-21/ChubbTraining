import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

export interface User {
  id?: string;
  name: string;
  email?: string;
}
@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = 'http://localhost:3000/users';

  constructor(private http: HttpClient) {}

  getUsers() {
    return this.http.get<any[]>(this.apiUrl);
  }

  getUserById(id: string) {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  addUser(user: any) {
    return this.http.post(this.apiUrl, user);
  }

  updateUser(id: string, user: User) {
    return this.http.put(`${this.apiUrl}/${id}`, user);
  }

  deleteUser(id: string) {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
