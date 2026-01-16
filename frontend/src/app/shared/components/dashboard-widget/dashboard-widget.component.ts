import { Component, Input, signal, computed } from '@angular/core';
import { RouterModule } from '@angular/router';
import { featureRegistry } from '../../../core/config/feature-registry';

export interface DashboardWidget {
  title: string;
  value: number;
  icon: string;
  route?: string;
  feature?: string;
}

@Component({
  selector: 'app-dashboard-widget',
  standalone: true,
  imports: [RouterModule],
  template: `
    <div class="card bg-base-100 shadow-xl">
      <div class="card-body">
        <div class="flex items-center justify-between">
          <div>
            <h3 class="card-title text-sm font-medium text-base-content/70">{{ title }}</h3>
            <p class="text-2xl font-bold">{{ value }}</p>
          </div>
          <div class="text-primary">
            <svg class="w-8 h-8" fill="currentColor" viewBox="0 0 20 20">
              <path [attr.d]="icon"></path>
            </svg>
          </div>
        </div>
        @if (route) {
          <div class="card-actions justify-end">
            <a [routerLink]="route" class="btn btn-primary btn-sm">View Details</a>
          </div>
        }
      </div>
    </div>
  `
})
export class DashboardWidgetComponent {
  @Input() title!: string;
  @Input() value!: number;
  @Input() icon!: string;
  @Input() route?: string;
}
