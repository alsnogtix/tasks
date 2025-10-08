import { Routes } from '@angular/router';
import { TaskListComponent } from './task-list/task-list';
import { LoginComponent } from './auth/login/login';
import { RegisterComponent } from './auth/register/register';
import { authGuard } from './guards/auth-guard'; 

export const routes: Routes = [
  {
    path: '', // The main page
    component: TaskListComponent,
    canActivate: [authGuard] // This protects the route
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  // Redirect any other path to the main page
  {
    path: '**',
    redirectTo: ''
  }
];