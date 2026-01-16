import { Component, OnInit, signal, ChangeDetectionStrategy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Reptile } from '../models/reptile.model';
import { ReptileService } from '../services/reptile.service';

@Component({
  selector: 'app-reptile-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './reptile-list.component.html',
  styleUrls: ['./reptile-list.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ReptileListComponent implements OnInit {
  private reptileService = inject(ReptileService);

  reptiles = signal<Reptile[]>([]);
  loading = signal<boolean>(false);
  error = signal<string | null>(null);

  ngOnInit(): void {
    this.loadReptiles();
  }

  loadReptiles(): void {
    this.loading.set(true);
    this.error.set(null);

    this.reptileService.getAllReptiles().subscribe({
      next: (reptiles) => {
        this.reptiles.set(reptiles);
        this.loading.set(false);
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set('Failed to load reptiles. Please try again.');
        console.error('Error loading reptiles:', err);
      }
    });
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'ACTIVE': return 'badge-success';
      case 'QUARANTINE': return 'badge-warning';
      case 'DECEASED': return 'badge-error';
      case 'SOLD': return 'badge-info';
      default: return 'badge-ghost';
    }
  }

  getGenderIcon(gender: string): string {
    switch (gender) {
      case 'MALE': return '♂';
      case 'FEMALE': return '♀';
      default: return '?';
    }
  }

  /**
   * Gets the URL for the highlight image of a reptile.
   * Returns null if no highlight image is set.
   */
  getHighlightImageUrl(reptile: Reptile): string | null {
    if (!reptile.highlightImageId) {
      return null;
    }
    return this.reptileService.getImageUrl(reptile.id, reptile.highlightImageId);
  }

  /**
   * Returns a placeholder image URL for reptiles without highlight images.
   */
  getPlaceholderImage(): string {
    return ''; // Using inline SVG in template instead
  }
}
