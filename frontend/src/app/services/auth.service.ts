import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  login(email: string, password: string) {
    console.log("Login called", email, password);
  }

  logout() {
    console.log("Logout");
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

}