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

        localStorage.setItem("token", res.token);
        localStorage.setItem("role", res.role);

        console.log("LOGIN OK", res);

        this.router.navigate(['/perfil']); // 👈 redirige a tu página

      }, err => {
        alert("Credenciales incorrectas");
      });
  }
}