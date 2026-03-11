import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private api = 'http://localhost:8081';

  constructor(private http: HttpClient) {}

  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
  }

  login(data: any): Observable<any> {
    return this.http.post(`${this.api}/auth/login`, data);
  }

  logout(): Observable<any> {
    return this.http.post(`${this.api}/auth/logout`, {}, {
      headers: this.getHeaders(),
      responseType: 'text'
    });
  }

  getAdminProfile(): Observable<any> {
    return this.http.get(`${this.api}/perfil/admin`, {
      headers: this.getHeaders()
    });
  }

  getPatientProfile(): Observable<any> {
    return this.http.get(`${this.api}/perfil/patient`, {
      headers: this.getHeaders()
    });
  }

  getProfessionalProfile(): Observable<any> {
    return this.http.get(`${this.api}/perfil/professional`, {
      headers: this.getHeaders()
    });
  }
}