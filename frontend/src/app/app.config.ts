import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { routes } from './app.routes';
import { authInterceptor } from './interceptors/auth.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [

    // Manejo global de errores del navegador
    provideBrowserGlobalErrorListeners(),

    // Configuración de rutas
    provideRouter(routes),

    // Cliente HTTP con interceptor para enviar el JWT automáticamente
    provideHttpClient(
      withInterceptors([
        authInterceptor
      ])
    )

  ]
};