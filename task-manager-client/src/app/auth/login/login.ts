import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Auth } from '../../services/auth';
import { LoginRequest } from '../../interfaces/login-request';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrls: ['./login.scss']
})
export class LoginComponent {
  credentials: LoginRequest = {};
  errorMessage: string = '';

  constructor(private auth: Auth, private router: Router) { }

  login(): void {
    this.auth.login(this.credentials).subscribe({
      next: () => {
        // Navigate to the main task list on successful login
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error('Login failed', err);
        this.errorMessage = 'Login failed. Please check your username and password.';
      }
    });
  }
}