import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html'
})
export class LoginComponent {

  email = "";
  password = "";

  constructor(private auth: AuthService) {}

  login() {
    this.auth.login(this.email, this.password)
      .subscribe(res => {

        localStorage.setItem("token", res.token);
        localStorage.setItem("role", res.role);

        console.log("LOGIN OK", res);

      });
  }

}
