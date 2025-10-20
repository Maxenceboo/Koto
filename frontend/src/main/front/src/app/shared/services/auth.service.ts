import { Injectable }              from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { LoginUser }               from '../models/auth.model';
import { UserState }               from '../models/user.model';
import { UserStore }               from '../store/user.store';
import { tap }                     from 'rxjs';

@Injectable({providedIn: 'root'})
class AuthService {
  constructor(private http: HttpClient, private userStore: UserStore) {
  }

  login (loginUser: LoginUser){
    let headers = new HttpHeaders({
      Authorization: 'Basic ' + btoa(`${loginUser.email}:${loginUser.password}`)
    });

    return this.http.get<UserState>('/api/auth/me', { headers, withCredentials: true })
               .pipe(tap((user: UserState) => {
                 if (user) {
                   this.userStore.login(user);
                 }
               }))
  }

}

export default AuthService;
