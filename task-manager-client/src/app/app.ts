import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterOutlet } from '@angular/router';
import { TaskListComponent } from './task-list/task-list'; // FIXED: Removed the '.ts' extension
import { Observable } from 'rxjs';
import { Auth } from './services/auth';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, TaskListComponent],
  templateUrl: './app.html', 
  styleUrl: './app.scss'
})
export class App {
  title = 'task-manager-client';
  isLoggedIn$: Observable<boolean>;

  constructor(private auth: Auth, private router: Router) {
    this.isLoggedIn$ = this.auth.isLoggedIn;
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
