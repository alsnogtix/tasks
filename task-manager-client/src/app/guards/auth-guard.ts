import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Auth } from '../services/auth';
import { map, take } from 'rxjs/operators';

export const authGuard: CanActivateFn = (route, state) => {
  const auth = inject(Auth);
  const router = inject(Router);

  return auth.isLoggedIn.pipe(
    take(1), // Take the latest value and complete
    map(isLoggedIn => {
      if (!isLoggedIn) {
        // If not logged in, redirect to the login page
        router.navigate(['/login']);
        return false;
      }
      // If logged in, allow access
      return true;
    })
  );
};