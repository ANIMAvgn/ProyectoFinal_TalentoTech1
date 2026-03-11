import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs/operators';
import { AdminService } from '../../services/admin.service';

@Component({
  selector: 'app-register-user',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register-user.html',
  styleUrl: './register-user.css'
})
export class RegisterUser {

  nombre = '';
  apellido = '';
  tipoDocumento = '';
  numeroDocumento = '';
  celular = '';
  ciudad = '';
  direccion = '';
  email = '';
  password = '';
  role = '';

  registering = false;

  successMessage = '';
  errorMessage = '';

  constructor(
    private adminService: AdminService,
    private cdr: ChangeDetectorRef
  ) {}

  registerUser(): void {

    this.registering = true;
    this.successMessage = '';
    this.errorMessage = '';
    this.cdr.detectChanges();

    const body = {
      nombre: this.nombre,
      apellido: this.apellido,
      tipoDocumento: this.tipoDocumento,
      numeroDocumento: this.numeroDocumento,
      celular: this.celular,
      ciudad: this.ciudad,
      direccion: this.direccion,
      email: this.email,
      password: this.password,
      role: this.role
    };

    this.adminService.createUser(body)
      .pipe(
        finalize(() => {
          this.registering = false;
          this.cdr.detectChanges();
        })
      )
      .subscribe({

        next: () => {

          this.successMessage = '✔ Usuario registrado correctamente';

          this.nombre = '';
          this.apellido = '';
          this.tipoDocumento = '';
          this.numeroDocumento = '';
          this.celular = '';
          this.ciudad = '';
          this.direccion = '';
          this.email = '';
          this.password = '';
          this.role = '';

          this.cdr.detectChanges();

          setTimeout(() => {
            this.successMessage = '';
            this.cdr.detectChanges();
          }, 4000);
        },

        error: (err) => {

          console.error(err);

          this.errorMessage =
            err?.error?.message || '❌ No se pudo registrar el usuario';

          this.cdr.detectChanges();

          setTimeout(() => {
            this.errorMessage = '';
            this.cdr.detectChanges();
          }, 4000);
        }

      });
  }
}