import { Injectable, Injector } from '@angular/core';
import { DashboardWidget, DashboardWidgetProvider } from '../config/feature-registry';
import { ReptileDashboardWidgetProvider } from '../../features/reptile-management/reptile-dashboard-widget.provider';
import { ReptileService } from '../../features/reptile-management/services/reptile.service';
import { featureRegistry } from '../config/feature-registry';

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private widgetProviders: Map<string, DashboardWidgetProvider> = new Map();

  constructor(private injector: Injector) {
    // Register widget providers for enabled features
    if (featureRegistry.isFeatureEnabled('reptileManagement')) {
      this.widgetProviders.set('reptileManagement', new ReptileDashboardWidgetProvider(this.injector.get(ReptileService)));
    }

    // Add more feature providers here as they are implemented
  }

  async getDashboardWidgets(userId: number): Promise<DashboardWidget[]> {
    const allWidgets: DashboardWidget[] = [];

    for (const provider of this.widgetProviders.values()) {
      try {
        const widgets = await provider.getWidgets(userId);
        allWidgets.push(...widgets);
      } catch (error) {
        console.error('Error loading dashboard widgets:', error);
      }
    }

    return allWidgets;
  }

  async loadDashboardData(userId: number): Promise<void> {
    const loadPromises: Promise<void>[] = [];

    for (const provider of this.widgetProviders.values()) {
      if (provider.loadData) {
        loadPromises.push(provider.loadData(userId));
      }
    }

    await Promise.all(loadPromises);
  }
}
