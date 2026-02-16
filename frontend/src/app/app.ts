import { Component, OnInit, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { SidebarComponent } from './shared/components/sidebar/sidebar';
import { SidebarService } from './shared/services/sidebar.service';
import { PerformanceService } from './core/services/performance.service';
import { AuthService } from './core/services/auth.service';


/**
 * Root component of the application.
 * Serves as the main application shell containing the router outlet and navigation.
 */
@Component({
  selector: 'app-root',
  imports: [RouterOutlet, CommonModule, SidebarComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  /** Application title displayed in the browser tab */
  protected title = 'reptile-management-app';

  sidebarService = inject(SidebarService);
  authService = inject(AuthService);
  private performanceService = inject(PerformanceService);

  ngOnInit(): void {
    this.performanceService.mark('app-init-start');
    // Mark app fully loaded after a short delay to ensure rendering
    setTimeout(() => {
      this.performanceService.mark('app-init-end');
      this.performanceService.measure('app-initialization', 'app-init-start', 'app-init-end');
      const duration = this.performanceService.getMeasureDuration('app-initialization');
      if (duration) {
        this.performanceService.logPerformance('App initialization time', duration);
      }
    }, 100);
  }
}
