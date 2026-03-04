import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';


@Component({
  selector: 'app-professionals',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './professionals.html',
  styleUrl: './professionals.css'
})
export class Professionals {

  professionals = [
    { id: 1, nombre: 'Ana', apellido: 'Perez', especialidad: 'Ansiedad' },
    { id: 2, nombre: 'Carlos', apellido: 'Lopez', especialidad: 'Depresión' }
  ];

  constructor(private router: Router) {}

  goToBooking(id: number) {
    this.router.navigate(['/booking', id]);
  }
}
