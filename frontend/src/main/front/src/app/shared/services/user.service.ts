import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CreateUser } from '../models/user.model';

type UserResponse = {}

@Injectable({ providedIn: 'root' })
class UserService {

  constructor(private http: HttpClient) {}

  register (createUser: CreateUser) {
    return this.http.post<UserResponse>(`/api/user`, createUser);
  }

}

export default UserService;
