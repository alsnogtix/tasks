import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http'; // <-- REQUIRED for services

import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    // This provides the core functionality needed to make HTTP requests
    provideHttpClient(), 
    
    // We kept the default zone change detection as requested
    provideZoneChangeDetection({ eventCoalescing: true }),
    
    // Router configuration
    provideRouter(routes)
  ]
};
