import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { TaskListComponent } from './task-list/task-list'; // FIXED: Removed the '.ts' extension

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, TaskListComponent],
  templateUrl: './app.html', 
  styleUrl: './app.scss'
})
export class App {
  title = 'task-manager-client';
}
