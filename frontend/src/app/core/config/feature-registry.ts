import { Routes } from '@angular/router';
import { authGuard } from '../guards/auth.guard';
import { adminGuard } from '../guards/admin.guard';
import { FEATURE_FLAGS } from './feature-flags';

export interface NavigationItem {
  label: string;
  route: string;
  icon: string;
  adminOnly?: boolean;
  feature?: string;
}

export interface FeatureConfig {
  name: string;
  enabled: boolean;
  routes?: Routes;
  navigation?: NavigationItem[];
}

export class FeatureRegistry {
  private features: Map<string, FeatureConfig> = new Map();

  registerFeature(config: FeatureConfig): void {
    this.features.set(config.name, config);
  }

  isFeatureEnabled(featureName: string): boolean {
    return this.features.get(featureName)?.enabled ?? false;
  }

  getEnabledRoutes(): Routes {
    const routes: Routes = [];

    for (const feature of this.features.values()) {
      if (feature.enabled && feature.routes) {
        routes.push(...feature.routes);
      }
    }

    return routes;
  }

  getEnabledNavigation(): NavigationItem[] {
    const navigation: NavigationItem[] = [];

    for (const feature of this.features.values()) {
      if (feature.enabled && feature.navigation) {
        navigation.push(...feature.navigation);
      }
    }

    // Sort so that adminOnly items appear last
    return navigation.sort((a, b) => {
      if (a.adminOnly && !b.adminOnly) return 1;
      if (!a.adminOnly && b.adminOnly) return -1;
      return 0;
    });
  }

  getFeatureRoutes(featureName: string): Routes {
    return this.features.get(featureName)?.routes ?? [];
  }

  getFeatureNavigation(featureName: string): NavigationItem[] {
    return this.features.get(featureName)?.navigation ?? [];
  }
}

// Create singleton instance
export const featureRegistry = new FeatureRegistry();

// Feature configurations
export const FEATURE_CONFIGS: FeatureConfig[] = [
  {
    name: 'reptile-management',
    enabled: FEATURE_FLAGS.reptileManagement,
    routes: [
      {
        path: 'reptiles',
        loadComponent: () => import('../../features/reptile-management/reptile-list/reptile-list.component').then(m => m.ReptileListComponent),
        canActivate: [authGuard]
      },
      {
        path: 'reptiles/add',
        loadComponent: () => import('../../features/reptile-management/add-reptile/add-reptile.component').then(m => m.AddReptileComponent),
        canActivate: [authGuard]
      },
      {
        path: 'reptiles/:id',
        loadComponent: () => import('../../features/reptile-management/reptile-detail/reptile-detail.component').then(m => m.ReptileDetailComponent),
        canActivate: [authGuard]
      }
    ]
  },
  {
    name: 'user-management',
    enabled: FEATURE_FLAGS.userManagement,
    routes: [
      {
        path: 'admin/users',
        loadComponent: () => import('../../features/admin/user-management/user-management.component').then(m => m.UserManagementComponent),
        canActivate: [authGuard, adminGuard]
      }
    ],
    navigation: [
      {
        label: 'User Management',
        route: '/admin/users',
        icon: 'M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z',
        adminOnly: true,
        feature: 'userManagement'
      }
    ]
  }
];

// Initialize features
FEATURE_CONFIGS.forEach(config => featureRegistry.registerFeature(config));
