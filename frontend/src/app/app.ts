import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NgIf } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NgIf, RouterModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  protected readonly title = signal('frontend');

  role: string | null = null;

  constructor() {
    if (typeof window !== 'undefined') {
      this.role = localStorage.getItem('role');
    }
  }

  isLoggedIn(): boolean {
    if (typeof window === 'undefined') return false;
    return !!localStorage.getItem('token');
  }

  getProfileRoute(): string {
    const role = localStorage.getItem('role');

    if (role === 'PATIENT') {
      return '/perfil/patient';
    }

    if (role === 'PROFESSIONAL') {
      return '/perfil/professional';
    }

    if (role === 'ADMIN') {
      return '/perfil/admin';
    }

    return '/login';
  }
}
