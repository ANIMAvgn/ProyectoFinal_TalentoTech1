import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-perfil-admin',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './perfil-admin.html',
  styleUrl: './perfil-admin.css'
})
export class PerfilAdmin implements OnInit {

  adminName: string = 'Administrador';
  errorMessage: string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.getAdminProfile().subscribe({
      next: (res: any) => {
        this.adminName = res?.message?.replace('Bienvenido, ', '') || 'Administrador';
      },
      error: () => {
        this.errorMessage = 'No se pudo cargar el perfil del administrador.';
      }
    });
  }

  logout(): void {
    this.authService.logout().subscribe({
      next: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        this.router.navigate(['/login']);
      },
      error: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        this.router.navigate(['/login']);
      }
    });
  }
}