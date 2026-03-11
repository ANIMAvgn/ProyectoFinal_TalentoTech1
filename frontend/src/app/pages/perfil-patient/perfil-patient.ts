import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-perfil-patient',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './perfil-patient.html',
  styleUrl: './perfil-patient.css'
})
export class PerfilPatient implements OnInit {

  patientName: string = 'Paciente';
  errorMessage: string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authService.getPatientProfile().subscribe({
      next: (res: any) => {
        this.patientName = res?.message?.replace('Bienvenido, ', '') || 'Paciente';
      },
      error: () => {
        this.errorMessage = 'No se pudo cargar el perfil del paciente.';
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