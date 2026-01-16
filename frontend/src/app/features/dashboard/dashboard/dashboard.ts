import { Component, OnInit, computed, ChangeDetectionStrategy, signal } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';
import { ReptileService } from '../../reptile-management/services/reptile.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DashboardComponent implements OnInit {
  reptileCount = signal(0);

  // Check if user is admin
  isAdmin = computed(() => this.authService.isAdmin());
  username = computed(() => this.authService.username());

  constructor(
    private authService: AuthService,
    private reptileService: ReptileService
  ) {}

  ngOnInit(): void {
    // Load reptile count when dashboard loads
    this.reptileService.getAllReptiles().subscribe({
      next: (reptiles: any[]) => this.reptileCount.set(reptiles.length),
      error: (err: any) => console.error('Error loading reptiles:', err)
    });
  }
}
