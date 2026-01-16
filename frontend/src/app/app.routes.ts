import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/admin.guard';
import { featureRegistry } from './core/config/feature-registry';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', loadComponent: () => import('./features/auth/login/login').then(m => m.LoginComponent) },
  {
    path: 'dashboard',
    loadComponent: () => import('./features/dashboard/dashboard/dashboard').then(m => m.DashboardComponent),
    canActivate: [authGuard]
  },
  {
    path: 'profile-settings',
    loadComponent: () => import('./features/profile-settings/profile-settings').then(m => m.ProfileSettings),
    canActivate: [authGuard]
  },
  // Feature-specific routes are added dynamically
  ...featureRegistry.getEnabledRoutes(),
  { path: '**', redirectTo: '/login' }
];
