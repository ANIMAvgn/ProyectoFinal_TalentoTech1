import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppointmentService } from '../../services/appointment.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-appointment-booking',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './appointment-booking.html',
  styleUrl: './appointment-booking.css'
})
export class AppointmentBooking implements OnInit {

  professionals: any[] = [];
  selectedDate: string = '';
  role: string | null = null;

  constructor(private appointmentService: AppointmentService) {}

  ngOnInit() {

    // evitar error SSR
    if (typeof window !== 'undefined') {
      this.role = localStorage.getItem("role");
    }

    // fecha por defecto = hoy
    const today = new Date();
    this.selectedDate = today.toISOString().split('T')[0];

    this.loadProfessionals();
  }

  loadProfessionals() {

    this.appointmentService
      .getProfessionals(this.selectedDate)
      .subscribe((data: any) => {

        this.professionals = data;

      });

  }

  reservar(professionalId: number, slot: any) {

    this.appointmentService
      .bookSlot(professionalId, slot.date, slot.time)
      .subscribe(() => {

        alert("Cita reservada correctamente");

        // recargar agenda
        this.loadProfessionals();

      });

  }

}