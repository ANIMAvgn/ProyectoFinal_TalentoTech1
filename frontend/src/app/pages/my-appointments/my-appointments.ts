import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppointmentService } from '../../services/appointment.service';

@Component({
  selector: 'app-my-appointments',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './my-appointments.html'
})
export class MyAppointments implements OnInit {

  appointments:any[] = [];

  constructor(private appointmentService:AppointmentService){}

  ngOnInit(){

    this.loadAppointments();

  }

  loadAppointments(){

  this.appointmentService
  .getMyAppointments()
  .subscribe((data:any)=>{

    setTimeout(()=>{
      this.appointments = data;
    });

  });

}

}
