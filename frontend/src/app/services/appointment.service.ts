import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  api = "http://localhost:8081";

  constructor(private http: HttpClient) {}

  getProfessionals(date: string) {

    if (typeof window === 'undefined') {
      return this.http.get(`${this.api}/patient/professionals?date=${date}`);
    }

    const token = localStorage.getItem("token");

    return this.http.get(
      `${this.api}/patient/professionals?date=${date}`,
      {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    );
  }

  getSlots(professionalId:number, date:string) {

  const token = localStorage.getItem("token");

  return this.http.get(
    `${this.api}/patient/professionals/${professionalId}/slots?date=${date}`,
    {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }
  );
}

  bookSlot(professionalId: number, date: string, time: string) {

    const token = localStorage.getItem("token");

    return this.http.post(
      `${this.api}/patient/appointments/book-from-slot`,
      {
      professionalId: professionalId,
      date: date,
      time: time
      },
      {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }
    );
  }

}

