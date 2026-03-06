import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';
import { Perfil } from './pages/perfil/perfil';
import { Professionals } from './pages/professionals/professionals';
import { AppointmentBooking } from './pages/appointment-booking/appointment-booking';

export const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'professionals', component: Professionals },
  { path: 'booking/:id', component: AppointmentBooking },
  { path: 'perfil', component: Perfil }
];
