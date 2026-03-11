import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs/operators';
import { AppointmentService } from '../../services/appointment.service';

@Component({
  selector: 'app-book-appointment',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './book-appointment.html',
  styleUrl: './book-appointment.css'
})
export class BookAppointment {

  selectedDate: string = '';
  professionals: any[] = [];
  selectedProfessional: any = null;
  slots: string[] = [];

  loadingProfessionals = false;
  loadingSlots = false;
  booking = false;

  errorMessage = '';
  successMessage = '';

  constructor(
    private appointmentService: AppointmentService,
    private cdr: ChangeDetectorRef
  ) {}

  onDateChange(): void {
    this.professionals = [];
    this.selectedProfessional = null;
    this.slots = [];
    this.errorMessage = '';
    this.successMessage = '';
    this.loadingProfessionals = false;
    this.loadingSlots = false;
    this.cdr.detectChanges();
  }

  searchProfessionals(): void {
    if (!this.selectedDate) {
      this.errorMessage = 'Debes seleccionar una fecha.';
      this.cdr.detectChanges();
      return;
    }

    this.loadingProfessionals = true;
    this.errorMessage = '';
    this.successMessage = '';
    this.professionals = [];
    this.selectedProfessional = null;
    this.slots = [];
    this.cdr.detectChanges();

    this.appointmentService.getProfessionalsByDate(this.selectedDate)
      .pipe(
        finalize(() => {
          this.loadingProfessionals = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: (data: any) => {
          const normalized = Array.isArray(data) ? [...data] : [];
          this.professionals = normalized;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Error al cargar profesionales:', err);
          this.errorMessage =
            err?.error?.message || 'No se pudieron cargar los profesionales.';
          this.cdr.detectChanges();
        }
      });
  }

  selectProfessional(professional: any): void {
    if (!this.selectedDate) {
      this.errorMessage = 'Debes seleccionar una fecha antes de consultar horarios.';
      this.cdr.detectChanges();
      return;
    }

    this.selectedProfessional = professional;
    this.slots = [];
    this.loadingSlots = true;
    this.errorMessage = '';
    this.successMessage = '';
    this.cdr.detectChanges();

    this.appointmentService
      .getSlotsByProfessional(professional.professionalId, this.selectedDate)
      .subscribe({
        next: (data: any) => {
          this.slots = Array.isArray(data?.availableSlots)
            ? [...data.availableSlots]
            : [];
          this.loadingSlots = false;
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Error al cargar horarios:', err);
          this.errorMessage =
            err?.error?.message || 'No se pudieron cargar los horarios disponibles.';
          this.loadingSlots = false;
          this.cdr.detectChanges();
        }
      });
  }

  bookSlot(time: string): void {
    if (!this.selectedProfessional || !this.selectedDate || !time) {
      this.errorMessage = 'Debes seleccionar una fecha, un profesional y una hora.';
      this.cdr.detectChanges();
      return;
    }

    this.booking = true;
    this.errorMessage = '';
    this.successMessage = '';
    this.cdr.detectChanges();

    const body = {
      professionalId: this.selectedProfessional.professionalId,
      date: this.selectedDate,
      time
    };

    this.appointmentService.bookFromSlot(body)
      .pipe(
        finalize(() => {
          this.booking = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({
        next: () => {
          this.successMessage = 'La cita fue reservada correctamente.';
          this.cdr.detectChanges();
          this.selectProfessional(this.selectedProfessional);
        },
        error: (err) => {
          console.error('Error al reservar cita:', err);
          this.errorMessage =
            err?.error?.message || 'No fue posible reservar la cita.';
          this.cdr.detectChanges();
        }
      });
  }
}