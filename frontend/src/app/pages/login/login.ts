import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html'
})
export class Login {

  email: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  login(){

    const body = {
      email: this.email,
      password: this.password
    };

    this.authService
    .login(body)
    .subscribe({

      next:(res:any)=>{

        localStorage.setItem('token', res.token);
      
        localStorage.setItem('role', res.role);
        console.log('TOKEN GUARDADO:', localStorage.getItem('token')); 
        console.log('ROLE GUARDADO:', localStorage.getItem('role'))

        if(res.role === 'ADMIN'){
          this.router.navigate(['/perfil/admin']);
        }

        if(res.role === 'PATIENT'){
          this.router.navigate(['/perfil/patient']);
        }

        if(res.role === 'PROFESSIONAL'){
          this.router.navigate(['/perfil/professional']);
        }

      },

      error:()=>{

        this.errorMessage = "Credenciales incorrectas";

      }

    });

  }

}