import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-perfil-doctores',
  templateUrl: './perfil-doctores.html',
  styleUrl: './perfil-doctores.css'
})
export class PerfilDoctores {

  constructor(
    private auth: AuthService,
    private router: Router
  ) {}

  logout() {
    this.auth.logout().subscribe(() => {

      localStorage.clear();
      this.router.navigate(['/login']);

    }, err => {

      localStorage.clear();
      this.router.navigate(['/login']);

    });
  }

}