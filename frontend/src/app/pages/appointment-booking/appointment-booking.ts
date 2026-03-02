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

  professionalId!: number;

  appointments = [
    { id: 1, hora: '10:00 AM', estado: 'DISPONIBLE' },
    { id: 2, hora: '11:00 AM', estado: 'DISPONIBLE' },
    { id: 3, hora: '12:00 PM', estado: 'RESERVADA' }
  ];

  constructor(private route: ActivatedRoute) {
    this.professionalId = Number(this.route.snapshot.paramMap.get('id'));
  }

  book(id: number) {
    alert('Cita reservada con éxito');
  }
}
