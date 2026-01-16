import { Component, computed, inject } from '@angular/core';

import { RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ThemeService, type Theme } from '../../../core/services/theme.service';
import { featureRegistry, NavigationItem } from '../../../core/config/feature-registry';
import { SidebarService } from '../../services/sidebar.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class SidebarComponent {
  private sidebarService = inject(SidebarService);

  collapsed = this.sidebarService.collapsed;

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

  toggleCollapsed(): void {
    this.sidebarService.collapsed.set(!this.collapsed());
  }
}
