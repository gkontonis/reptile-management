import { DashboardWidgetProvider, DashboardWidget } from '@core/config/feature-registry';
import { ReptileService } from '@features/reptile-management/services/reptile.service';

export class ReptileDashboardWidgetProvider implements DashboardWidgetProvider {

  constructor(private reptileService: ReptileService) {}

  async getWidgets(userId: number): Promise<DashboardWidget[]> {
    try {
      const stats = await this.reptileService.getReptileStats().toPromise();

      return [
        {
          title: 'Total Reptiles',
          value: stats?.totalReptiles ?? 0,
          icon: 'M14.828 14.828a4 4 0 01-5.656 0M9 10h1.586a1 1 0 01.707.293l.707.707A1 1 0 0012.414 11H13m-3 3.5A2.5 2.5 0 1110.5 16v-1.5a1 1 0 10-2 0v1.5z',
          route: '/reptiles'
        },
        {
          title: 'Active Reptiles',
          value: stats?.activeReptiles ?? 0,
          icon: 'M14.828 14.828a4 4 0 01-5.656 0M9 10h1.586a1 1 0 01.707.293l.707.707A1 1 0 0012.414 11H13m-3 3.5A2.5 2.5 0 1110.5 16v-1.5a1 1 0 10-2 0v1.5z',
          route: '/reptiles'
        },
        {
          title: 'Need Feeding',
          value: stats?.needsFeeding ?? 0,
          icon: 'M12 9v3m0 0v3m0-3h3m-3 0H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z',
          route: '/reptiles'
        },
        {
          title: 'Enclosures Need Cleaning',
          value: stats?.needsCleaning ?? 0,
          icon: 'M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16',
          route: '/reptiles'
        }
      ];
    } catch (error) {
      console.error('Error loading reptile dashboard widgets:', error);
      return [];
    }
  }

  async loadData(userId: number): Promise<void> {
    // Pre-load any necessary data for the dashboard widgets
    // This could be used to cache data or perform background loading
    try {
      await this.reptileService.getReptileStats().toPromise();
    } catch (error) {
      console.error('Error pre-loading reptile dashboard data:', error);
    }
  }
}
