import { Component, computed } from '@angular/core';

import { RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ThemeService, type Theme } from '../../../core/services/theme.service';
import { featureRegistry, NavigationItem } from '../../../core/config/feature-registry';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class NavbarComponent {
  username = computed(() => this.authService.username() || 'User');
  isAuthenticated = computed(() => this.authService.isAuthenticated());
  isAdmin = computed(() => this.authService.isAdmin());

  // Get navigation items based on enabled features and user permissions
  navigationItems = computed(() => {
    const allItems = featureRegistry.getEnabledNavigation();
    return allItems.filter(item => !item.adminOnly || this.isAdmin());
  });

  constructor(
    public authService: AuthService,
    public themeService: ThemeService
  ) {}

  logout(): void {
    this.authService.logout();
  }

  setTheme(theme: Theme): void {
    this.themeService.setTheme(theme);
  }
}
