import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService as Auth} from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './register.html',
  styleUrls: ['./register.scss']
})
export class RegisterComponent {
  newUser = {
    username: '',
    password: ''
  };
  errorMessage: string = '';

  constructor(private auth: Auth, private router: Router) { }

  register(): void {
    this.auth.register(this.newUser).subscribe({
      next: () => {
        // After successful registration, navigate to the login page
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Registration failed', err);
        this.errorMessage = 'Registration failed. That username might already be taken.';
      }
    });
  }
}