import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  api = "http://localhost:8081";

  constructor(private http: HttpClient) {}

  getProfessionals(date: string) {
    return this.http.get(`${this.api}/patient/professionals?date=${date}`);
  }

  bookSlot(professionalId: number, date: string, time: string) {
    return this.http.post(`${this.api}/patient/appointments/book-slot`, {
      professionalId,
      date,
      time
    });
  }

}