import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-perfil-pacientes',
  templateUrl: './perfil-pacientes.html',
  styleUrl: './perfil-pacientes.css'
})
export class PerfilPacientes {

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
