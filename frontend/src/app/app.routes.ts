import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';
import { Perfil } from './pages/perfil/perfil';
import { Professionals } from './pages/professionals/professionals';
import { LandingComponent } from './pages/landing/landing';
import { AppointmentBooking } from './pages/appointment-booking/appointment-booking';
import { PerfilPacientes } from './pages/perfil-pacientes/perfil-pacientes';
import { PerfilDoctores } from './pages/perfil-doctores/perfil-doctores';
import { MyAppointments } from './pages/my-appointments/my-appointments';

export const routes: Routes = [
  { path: '', component: LandingComponent },   // landing page
  { path: 'login', component: LoginComponent },
  { path: 'professionals', component: Professionals },
  { path: 'appointment-booking', component: AppointmentBooking },
  { path: 'perfil', component: Perfil },                // admin
  { path: 'perfil-pacientes', component: PerfilPacientes }, // patient
  { path: 'perfil-doctores', component: PerfilDoctores },   // doctor
  { path:'my-appointments', component: MyAppointments }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}