import { Routes } from '@angular/router';
import { Landing } from './pages/landing/landing';
import { Login } from './pages/login/login';
import { PerfilAdmin } from './pages/perfil-admin/perfil-admin';
import { PerfilPatient } from './pages/perfil-patient/perfil-patient';
import { PerfilProfessional } from './pages/perfil-professional/perfil-professional';
import { MyAppointments } from './pages/my-appointments/my-appointments';
import { BookAppointment } from './pages/book-appointment/book-appointment';
// cuando tengas la vista del profesional:
import { ProfessionalAppointments } from './pages/professional-appointments/professional-appointments';
import { RegisterUser } from './pages/register-user/register-user';

export const routes: Routes = [
  { path: '', component: Landing },
  { path: 'login', component: Login },
   { path: 'register-user', component: RegisterUser },

  { path: 'perfil/admin', component: PerfilAdmin },
  { path: 'perfil/patient', component: PerfilPatient },
  { path: 'perfil/professional', component: PerfilProfessional },

  { path: 'patient/citas', component: MyAppointments },
  { path: 'patient/reservar-cita', component: BookAppointment },

  { path: 'professional/citas', component: ProfessionalAppointments },

  { path: '**', redirectTo: '' }
];