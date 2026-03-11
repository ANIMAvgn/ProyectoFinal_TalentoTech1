import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-perfil-professional',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './perfil-professional.html',
  styleUrl: './perfil-professional.css'
})
export class PerfilProfessional implements OnInit {

  professionalName: string = 'Profesional';
  errorMessage: string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.getProfessionalProfile().subscribe({
      next: (res: any) => {
        this.professionalName = res?.message?.replace('Bienvenido, ', '') || 'Profesional';
      },
      error: () => {
        this.errorMessage = 'No se pudo cargar el perfil del profesional.';
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