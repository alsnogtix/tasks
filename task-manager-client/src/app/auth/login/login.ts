import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
// --- FIX: Changed 'Auth' to 'AuthService' --- 
import { LoginRequest } from '../../interfaces/login-request';
import { AuthService } from '../../services/auth.service';

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

  // --- FIX: Changed 'Auth' to 'AuthService' ---
  constructor(private authService: AuthService, private router: Router) { }

  login(): void {
    // --- FIX: Changed 'auth' to 'authService' ---
    this.authService.login(this.credentials).subscribe({
      next: () => {
        // Navigate to the main task list on successful login
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error('Login failed', err);
        // It's helpful to show the actual error status for debugging
        this.errorMessage = `Login failed (Error: ${err.status}). Please check your username and password.`;
      }
    });
  }
}