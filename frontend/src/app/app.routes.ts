import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login';
import { Perfil } from './pages/perfil/perfil';
import { Professionals } from './pages/professionals/professionals';
import { LandingComponent } from './pages/landing/landing';
import { AppointmentBooking } from './pages/appointment-booking/appointment-booking';


export const routes: Routes = [
  { path: '', component: LandingComponent },   // landing page
  { path: 'login', component: LoginComponent },
  { path: 'professionals', component: Professionals },
  { path: 'booking/:id', component: AppointmentBooking },
  { path: 'perfil', component: Perfil }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}