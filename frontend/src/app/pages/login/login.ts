import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html'
})
export class LoginComponent {

  email = "";
  password = "";

  constructor(
    private auth: AuthService,
    private router: Router
  ) {}

  login() {
    this.auth.login(this.email, this.password)
      .subscribe(res => {

        console.log("LOGIN OK", res);

        const role = res.role;

        localStorage.setItem('token', res.token);
        localStorage.setItem('role', role);

        if (role === 'PATIENT') {
          this.router.navigate(['/perfil-pacientes']);
        } 
        else if (role === 'PROFESSIONAL') {
          this.router.navigate(['/perfil-doctores']);
        } 
        else if (role === 'ADMIN') {
          this.router.navigate(['/perfil']);
        }

      }, err => {
        alert("Credenciales incorrectas");
      });
  }
}