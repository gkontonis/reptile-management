import { Component, OnInit, signal, computed, ChangeDetectionStrategy, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Reptile } from '../models/reptile.model';
import { ReptileService } from '../services/reptile.service';
import { PrototypeVariantSwitcherComponent, PrototypeVariantOption } from '../../../shared/components/prototype-variant-switcher/prototype-variant-switcher.component';

@Component({
  selector: 'app-reptile-list',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule, PrototypeVariantSwitcherComponent],
  templateUrl: './reptile-list.component.html',
  styleUrls: ['./reptile-list.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class ReptileListComponent implements OnInit {
  private reptileService = inject(ReptileService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  reptiles = signal<Reptile[]>([]);
  loading = signal<boolean>(false);
  error = signal<string | null>(null);

  // Search and filter state
  searchQuery = signal<string>('');
  selectedSpecies = signal<string>('all');
  sortBy = signal<string>('recent');


  // PROTOTYPE: ?variant= switches the landing page layout while reusing
  // the same data below. See reptile-list.component.html for the three
  // variants. Drop this signal, the switcher, and the losing variants
  // once one is picked.
  variant = signal<'A' | 'B' | 'C'>('A');
  readonly variantOptions: PrototypeVariantOption[] = [
    { key: 'A', label: 'Toolbar, no sidebar' },
    { key: 'B', label: 'Table view' },
    { key: 'C', label: 'Hero stats + chips' }
  ];

  setVariant(key: string): void {
    if (key !== 'A' && key !== 'B' && key !== 'C') return;
    this.variant.set(key);
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { variant: key },
      queryParamsHandling: 'merge',
      replaceUrl: true
    });
  }
  // Get unique species from reptiles for the filter dropdown
  uniqueSpecies = computed(() => {
    const species = new Set(this.reptiles().map(r => r.species));
    return Array.from(species).sort();
  });

  // Filtered and sorted reptiles
  filteredReptiles = computed(() => {
    let result = this.reptiles();

    // Apply search filter
    const query = this.searchQuery().toLowerCase();
    if (query) {
      result = result.filter(r =>
        r.name.toLowerCase().includes(query) ||
        r.species.toLowerCase().includes(query) ||
        (r.subspecies?.toLowerCase().includes(query) ?? false)
      );
    }

    // Apply species filter
    if (this.selectedSpecies() !== 'all') {
      result = result.filter(r => r.species === this.selectedSpecies());
    }

    // Apply sorting
    switch (this.sortBy()) {
      case 'name':
        result = [...result].sort((a, b) => a.name.localeCompare(b.name));
        break;
      case 'status':
        result = [...result].sort((a, b) => a.status.localeCompare(b.status));
        break;
      case 'recent':
      default:
        result = [...result].sort((a, b) =>
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
        break;
    }

    return result;
  });

  // Stats
  totalCount = computed(() => this.reptiles().length);
  activeCount = computed(() => this.reptiles().filter(r => r.status === 'ACTIVE').length);

  ngOnInit(): void {
    const variantParam = this.route.snapshot.queryParamMap.get('variant');
    if (variantParam === 'A' || variantParam === 'B' || variantParam === 'C') {
      this.variant.set(variantParam);
    }
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

  onSearch(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.searchQuery.set(value);
  }

  onSpeciesChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.selectedSpecies.set(value);
  }

  onSortChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.sortBy.set(value);
  }

  clearFilters(): void {
    this.searchQuery.set('');
    this.selectedSpecies.set('all');
    this.sortBy.set('recent');
  }

  getStatusBadgeClass(status: string): { bg: string; text: string } {
    switch (status) {
      case 'ACTIVE':
        return { bg: 'badge-success', text: 'text-success-content' };
      case 'QUARANTINE':
        return { bg: 'badge-warning', text: 'text-warning-content' };
      case 'DECEASED':
        return { bg: 'badge-error', text: 'text-error-content' };
      case 'SOLD':
        return { bg: 'badge-info', text: 'text-info-content' };
      default:
        return { bg: 'badge-ghost', text: '' };
    }
  }

  getAge(birthDate: string | undefined): string {
    if (!birthDate) return 'Unknown';
    const birth = new Date(birthDate);
    const now = new Date();

    let years = now.getFullYear() - birth.getFullYear();
    let months = now.getMonth() - birth.getMonth();

    if (months < 0) {
      years--;
      months += 12;
    }

    if (years === 0) {
      return `${months}m`;
    }
    if (months === 0) {
      return `${years}y`;
    }
    return `${years}y ${months}m`;
  }

  getHighlightImageUrl(reptile: Reptile): string | null {
    if (!reptile.highlightImageId) {
      return null;
    }
    return this.reptileService.getImageUrl(reptile.id, reptile.highlightImageId);
  }

  navigateToDetail(reptileId: number): void {
    this.router.navigate(['/reptiles', reptileId]);
  }

  navigateToAdd(): void {
    this.router.navigate(['/reptiles/add']);
  }
}
