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

  // solo ejecutar en navegador
  if (typeof window === 'undefined') {
    return;
  }

  const today = new Date();
  this.selectedDate = today.toISOString().split('T')[0];

  this.loadProfessionals();
}

  loadProfessionals() {

  this.appointmentService
    .getProfessionals(this.selectedDate)
    .subscribe((data:any) => {

      this.professionals = data;

      // cargar slots de cada doctor
      this.professionals.forEach((p:any) => {

        this.appointmentService.getSlots(p.professionalId, this.selectedDate).subscribe((res:any) => {

            p.slots = res.availableSlots;

          });

      });

    });

  }

  reservar(professionalId: number, slot: string) {

  const startAt = `${this.selectedDate}T${slot}:00-05:00`;

  console.log("START AT:", startAt);

  this.appointmentService.bookSlot(professionalId, startAt)
    .subscribe({
      next: () => {
        alert("Cita reservada");
      },
      error: err => {
        console.error(err);
      }
    });
}

}