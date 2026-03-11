import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { finalize } from 'rxjs/operators';
import { AppointmentService } from '../../services/appointment.service';

@Component({
  selector: 'app-professional-appointments',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './professional-appointments.html',
  styleUrl: './professional-appointments.css'
})
export class ProfessionalAppointments implements OnInit {

  appointments: any[] = [];
  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private appointmentService: AppointmentService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadAppointments();
  }

  loadAppointments(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';
    this.cdr.detectChanges();

    this.appointmentService.getProfessionalAppointments()
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: (data: any) => {
          this.appointments = Array.isArray(data) ? [...data] : [];
          console.log('Citas del profesional:', this.appointments);
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Error al cargar citas del profesional:', err);
          this.errorMessage =
            err?.error?.message || 'No se pudieron cargar las citas asignadas.';
          this.cdr.detectChanges();
        }
      });
  }

  cancelAppointment(id: number): void {
    if (!confirm('¿Deseas cancelar esta cita?')) return;

    this.errorMessage = '';
    this.successMessage = '';
    this.cdr.detectChanges();

    this.appointmentService.cancelProfessionalAppointment(id).subscribe({
      next: () => {
        this.successMessage = 'La cita fue cancelada correctamente.';
        this.cdr.detectChanges();
        this.loadAppointments();
      },
      error: (err) => {
        console.error('Error al cancelar cita:', err);
        this.errorMessage =
          err?.error?.message || 'No fue posible cancelar la cita.';
        this.cdr.detectChanges();
      }
    });
  }

  getStatusClass(status: string): string {
    if (status === 'BOOKED') return 'badge-booked';
    if (status === 'CANCELLED') return 'badge-cancelled';
    return 'badge-default';
  }
}