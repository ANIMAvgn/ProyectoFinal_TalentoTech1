import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  private api = 'http://localhost:8081';

  constructor(private http: HttpClient) {}

  getMyAppointments(): Observable<any> {
    return this.http.get(`${this.api}/patient/appointments`);
  }

  cancelMyAppointment(id: number): Observable<any> {
    return this.http.post(`${this.api}/patient/appointments/${id}/cancel`, {});
  }

  getProfessionalsByDate(date: string): Observable<any> {
    return this.http.get(`${this.api}/patient/professionals?date=${date}`);
  }

  getSlotsByProfessional(professionalId: number, date: string): Observable<any> {
    return this.http.get(`${this.api}/patient/professionals/${professionalId}/slots?date=${date}`);
  }

  bookFromSlot(body: any): Observable<any> {
    return this.http.post(`${this.api}/patient/appointments/book-from-slot`, body);
  }

  getProfessionalAppointments(): Observable<any> {
    return this.http.get(`${this.api}/professional/appointments`);
  }

  cancelProfessionalAppointment(id: number): Observable<any> {
    return this.http.post(`${this.api}/professional/appointments/${id}/cancel`, {});
  }
}