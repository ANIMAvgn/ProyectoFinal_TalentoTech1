import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-appointment-booking',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './appointment-booking.html',
  styleUrl: './appointment-booking.css'
})
export class AppointmentBooking {

  professionals = [
    {
      id: 1,
      nombre: 'Dra. Ana Pérez',
      disponibilidad: [
        { fecha: '2026-03-10', hora: '10:00 AM', estado: 'DISPONIBLE' },
        { fecha: '2026-03-10', hora: '11:00 AM', estado: 'RESERVADA' }
      ]
    },
    {
      id: 2,
      nombre: 'Dr. Carlos López',
      disponibilidad: [
        { fecha: '2026-03-11', hora: '09:00 AM', estado: 'DISPONIBLE' },
        { fecha: '2026-03-11', hora: '10:00 AM', estado: 'DISPONIBLE' }
      ]
    }
  ];

}
