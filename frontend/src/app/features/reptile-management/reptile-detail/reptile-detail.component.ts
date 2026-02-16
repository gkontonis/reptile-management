import { Component, OnInit, signal, inject, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ReptileDetail, ReptileImage, FeedingLog, SheddingLog, EnclosureCleaning } from '../models/reptile.model';
import { ReptileService } from '../services/reptile.service';

@Component({
  selector: 'app-reptile-detail',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule, FormsModule],
  templateUrl: './reptile-detail.component.html',
  styleUrls: ['./reptile-detail.component.css']
})
export class ReptileDetailComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private reptileService = inject(ReptileService);
  private fb = inject(FormBuilder);

  reptile = signal<ReptileDetail | null>(null);
  loading = signal<boolean>(false);
  error = signal<string | null>(null);

  // Tab state
  activeTab = signal<'overview' | 'feeding' | 'health'>('overview');

  // Feeding logs
  feedingLogs = signal<FeedingLog[]>([]);
  feedingLogsLoading = signal<boolean>(false);

  // Shedding logs
  sheddingLogs = signal<SheddingLog[]>([]);
  sheddingLogsLoading = signal<boolean>(false);

  // Cleaning logs
  cleaningLogs = signal<EnclosureCleaning[]>([]);
  cleaningLogsLoading = signal<boolean>(false);

  // Derived dates from logs
  lastFedDate = computed(() => {
    const logs = this.feedingLogs();
    return logs.length > 0 ? logs[0].feedingDate : undefined;
  });

  lastShedDate = computed(() => {
    const logs = this.sheddingLogs();
    return logs.length > 0 ? logs[0].sheddingDate : undefined;
  });

  lastCleanedDate = computed(() => {
    const logs = this.cleaningLogs();
    return logs.length > 0 ? logs[0].cleaningDate : undefined;
  });

  // Modal states
  showDeleteModal = signal<boolean>(false);
  showEditModal = signal<boolean>(false);
  showFeedingModal = signal<boolean>(false);
  showSheddingModal = signal<boolean>(false);
  showCleaningModal = signal<boolean>(false);
  showActivityMenu = signal<boolean>(false);
  deleteLoading = signal<boolean>(false);
  editLoading = signal<boolean>(false);
  feedingLoading = signal<boolean>(false);
  sheddingLoading = signal<boolean>(false);
  cleaningLoading = signal<boolean>(false);

  // Edit form
  editForm: FormGroup = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    species: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    subspecies: ['', Validators.maxLength(100)],
    morph: ['', Validators.maxLength(100)],
    breeder: ['', Validators.maxLength(100)],
    purchasePrice: [null, [Validators.min(0)]],
    gender: ['UNKNOWN', Validators.required],
    status: ['ACTIVE', Validators.required],
    acquisitionDate: ['', Validators.required],
    birthDate: [''],
    enclosureId: [null],
    notes: ['', Validators.maxLength(1000)]
  });

  // Feeding form
  feedingForm: FormGroup = this.fb.group({
    feedingDate: ['', Validators.required],
    foodType: ['', [Validators.required, Validators.maxLength(100)]],
    quantity: ['', [Validators.required, Validators.maxLength(100)]],
    ate: [true],
    notes: ['', Validators.maxLength(500)]
  });

  foodTypeOptions = [
    'Mouse - Pinky',
    'Mouse - Fuzzy',
    'Mouse - Hopper',
    'Mouse - Adult',
    'Rat - Pinky',
    'Rat - Fuzzy',
    'Rat - Pup',
    'Rat - Weaned',
    'Rat - Small',
    'Rat - Medium',
    'Rat - Large',
    'Crickets',
    'Dubia Roaches',
    'Mealworms',
    'Superworms',
    'Hornworms',
    'Waxworms',
    'Vegetables',
    'Fruit'
  ];

  // Shedding form
  sheddingForm: FormGroup = this.fb.group({
    sheddingDate: ['', Validators.required],
    shedQuality: ['COMPLETE', Validators.required],
    ateShed: [false],
    notes: ['', Validators.maxLength(500)]
  });

  shedQualityOptions = [
    { value: 'COMPLETE', label: 'Complete' },
    { value: 'PARTIAL', label: 'Partial' },
    { value: 'INCOMPLETE', label: 'Incomplete' }
  ];

  // Cleaning form
  cleaningForm: FormGroup = this.fb.group({
    cleaningDate: ['', Validators.required],
    cleaningType: ['SPOT_CLEAN', Validators.required],
    substrateChanged: [false],
    disinfected: [false],
    notes: ['', Validators.maxLength(500)]
  });

  cleaningTypeOptions = [
    { value: 'SPOT_CLEAN', label: 'Spot Clean' },
    { value: 'FULL_CLEAN', label: 'Full Clean' },
    { value: 'WATER_CHANGE', label: 'Water Change' },
    { value: 'DEEP_CLEAN', label: 'Deep Clean' }
  ];

  genderOptions = [
    { value: 'MALE', label: 'Male' },
    { value: 'FEMALE', label: 'Female' },
    { value: 'UNKNOWN', label: 'Unknown' }
  ];

  statusOptions = [
    { value: 'ACTIVE', label: 'Active' },
    { value: 'QUARANTINE', label: 'Quarantine' },
    { value: 'DECEASED', label: 'Deceased' },
    { value: 'SOLD', label: 'Sold' }
  ];

  // Image state
  images = signal<ReptileImage[]>([]);
  imagesLoading = signal<boolean>(false);
  showUploadModal = signal<boolean>(false);
  uploadLoading = signal<boolean>(false);
  selectedFile: File | null = null;
  imageDescription = '';

  ngOnInit(): void {
    const id = this.route.snapshot.params['id'];
    if (id) {
      this.loadReptile(+id);
      this.loadImages(+id);
      this.loadFeedingLogs(+id);
      this.loadSheddingLogs(+id);
    }
  }

  setActiveTab(tab: 'overview' | 'feeding' | 'health'): void {
    this.activeTab.set(tab);
  }

  loadFeedingLogs(reptileId: number): void {
    this.feedingLogsLoading.set(true);
    this.reptileService.getFeedingLogs(reptileId).subscribe({
      next: (logs) => {
        const sorted = logs.sort((a, b) =>
          new Date(b.feedingDate).getTime() - new Date(a.feedingDate).getTime()
        );
        this.feedingLogs.set(sorted);
        this.feedingLogsLoading.set(false);
      },
      error: (err) => {
        this.feedingLogsLoading.set(false);
        console.error('Error loading feeding logs:', err);
      }
    });
  }

  getDaysAgo(dateString: string | undefined): string {
    if (!dateString) return 'Never';
    const date = new Date(dateString);
    const now = new Date();
    const diffTime = Math.abs(now.getTime() - date.getTime());
    const diffDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));

    if (diffDays === 0) return 'Today';
    if (diffDays === 1) return '1 day ago';
    return `${diffDays} days ago`;
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
      return months === 1 ? '1 month' : `${months} months`;
    }
    if (months === 0) {
      return years === 1 ? '1 year' : `${years} years`;
    }
    return `${years} year${years > 1 ? 's' : ''}, ${months} month${months > 1 ? 's' : ''}`;
  }

  getHighlightImageUrl(): string | null {
    const reptile = this.reptile();
    if (!reptile?.highlightImageId) return null;
    return this.reptileService.getImageUrl(reptile.id, reptile.highlightImageId);
  }

  loadReptile(id: number): void {
    this.loading.set(true);
    this.error.set(null);

    this.reptileService.getReptileById(id).subscribe({
      next: (reptile) => {
        this.reptile.set(reptile);
        this.loading.set(false);
        this.loadCleaningLogs(reptile.enclosureId);
      },
      error: (err) => {
        this.loading.set(false);
        this.error.set('Failed to load reptile details. Please try again.');
        console.error('Error loading reptile details:', err);
      }
    });
  }

  loadImages(reptileId: number): void {
    this.imagesLoading.set(true);
    this.reptileService.getImages(reptileId).subscribe({
      next: (images) => {
        this.images.set(images);
        this.imagesLoading.set(false);
      },
      error: (err) => {
        this.imagesLoading.set(false);
        console.error('Error loading images:', err);
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

  onBack(): void {
    this.router.navigate(['/reptiles']);
  }

  onEdit(): void {
    const reptile = this.reptile();
    if (reptile) {
      // Populate form with current reptile data
      this.editForm.patchValue({
        name: reptile.name,
        species: reptile.species,
        subspecies: reptile.subspecies || '',
        morph: reptile.morph || '',
        breeder: reptile.breeder || '',
        purchasePrice: reptile.purchasePrice || null,
        gender: reptile.gender,
        status: reptile.status,
        acquisitionDate: reptile.acquisitionDate,
        birthDate: reptile.birthDate || '',
        enclosureId: reptile.enclosureId || null,
        notes: reptile.notes || ''
      });
      this.showEditModal.set(true);
    }
  }

  onDelete(): void {
    this.showDeleteModal.set(true);
  }

  confirmDelete(): void {
    const reptile = this.reptile();
    if (!reptile) return;

    this.deleteLoading.set(true);
    this.reptileService.deleteReptile(reptile.id).subscribe({
      next: () => {
        this.deleteLoading.set(false);
        this.showDeleteModal.set(false);
        this.router.navigate(['/reptiles']);
      },
      error: (err) => {
        this.deleteLoading.set(false);
        this.error.set('Failed to delete reptile. Please try again.');
        console.error('Error deleting reptile:', err);
        this.showDeleteModal.set(false);
      }
    });
  }

  cancelDelete(): void {
    this.showDeleteModal.set(false);
  }

  onSubmitEdit(): void {
    if (this.editForm.valid) {
      const reptile = this.reptile();
      if (!reptile) return;

      this.editLoading.set(true);
      const formValue = this.editForm.value;

      // Convert date strings to proper format
      const reptileData: Partial<ReptileDetail> = {
        ...formValue,
        acquisitionDate: formValue.acquisitionDate ? new Date(formValue.acquisitionDate).toISOString().split('T')[0] : undefined,
        birthDate: formValue.birthDate ? new Date(formValue.birthDate).toISOString().split('T')[0] : undefined
      };

      this.reptileService.updateReptile(reptile.id, reptileData).subscribe({
        next: (updatedReptile) => {
          this.editLoading.set(false);
          this.showEditModal.set(false);
          // Reload all reptile data
          this.loadReptile(reptile.id);
          this.loadImages(reptile.id);
          this.loadFeedingLogs(reptile.id);
        },
        error: (err) => {
          this.editLoading.set(false);
          this.error.set('Failed to update reptile. Please try again.');
          console.error('Error updating reptile:', err);
        }
      });
    } else {
      // Mark all fields as touched to show validation errors
      Object.keys(this.editForm.controls).forEach(key => {
        this.editForm.get(key)?.markAsTouched();
      });
    }
  }

  cancelEdit(): void {
    this.showEditModal.set(false);
    this.editForm.reset();
  }

  getFieldError(fieldName: string): string | null {
    const field = this.editForm.get(fieldName);
    if (field && field.errors && field.touched) {
      if (field.errors['required']) {
        return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} is required`;
      }
      if (field.errors['minlength']) {
        return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} must be at least ${field.errors['minlength'].requiredLength} characters`;
      }
      if (field.errors['maxlength']) {
        return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} must be no more than ${field.errors['maxlength'].requiredLength} characters`;
      }
      if (field.errors['min']) {
        return `${fieldName.charAt(0).toUpperCase() + fieldName.slice(1)} must be at least ${field.errors['min'].min}`;
      }
    }
    return null;
  }

  // Feeding log methods
  openFeedingModal(): void {
    const now = new Date();
    const localIso = new Date(now.getTime() - now.getTimezoneOffset() * 60000).toISOString().slice(0, 16);
    this.feedingForm.reset({ feedingDate: localIso, ate: true, foodType: '', quantity: '', notes: '' });
    this.showFeedingModal.set(true);
    this.showActivityMenu.set(false);
  }

  cancelFeeding(): void {
    this.showFeedingModal.set(false);
    this.feedingForm.reset();
  }

  onSubmitFeeding(): void {
    if (this.feedingForm.valid) {
      const reptile = this.reptile();
      if (!reptile) return;

      this.feedingLoading.set(true);
      const formValue = this.feedingForm.value;

      const log = {
        reptileId: reptile.id,
        feedingDate: new Date(formValue.feedingDate).toISOString(),
        foodType: formValue.foodType,
        quantity: formValue.quantity,
        ate: formValue.ate,
        notes: formValue.notes || null
      };

      this.reptileService.addFeedingLog(log).subscribe({
        next: () => {
          this.feedingLoading.set(false);
          this.showFeedingModal.set(false);
          this.feedingForm.reset();
          this.loadFeedingLogs(reptile.id);
          this.loadReptile(reptile.id);
        },
        error: (err) => {
          this.feedingLoading.set(false);
          console.error('Error creating feeding log:', err);
          this.error.set('Failed to create feeding log. Please try again.');
        }
      });
    } else {
      Object.keys(this.feedingForm.controls).forEach(key => {
        this.feedingForm.get(key)?.markAsTouched();
      });
    }
  }

  onDeleteFeedingLog(logId: number): void {
    const reptile = this.reptile();
    if (!reptile) return;

    this.reptileService.deleteFeedingLog(logId).subscribe({
      next: () => {
        this.loadFeedingLogs(reptile.id);
      },
      error: (err) => {
        console.error('Error deleting feeding log:', err);
        this.error.set('Failed to delete feeding log. Please try again.');
      }
    });
  }

  // Shedding log methods
  loadSheddingLogs(reptileId: number): void {
    this.sheddingLogsLoading.set(true);
    this.reptileService.getSheddingLogs(reptileId).subscribe({
      next: (logs) => {
        const sorted = logs.sort((a, b) =>
          new Date(b.sheddingDate).getTime() - new Date(a.sheddingDate).getTime()
        );
        this.sheddingLogs.set(sorted);
        this.sheddingLogsLoading.set(false);
      },
      error: (err) => {
        this.sheddingLogsLoading.set(false);
        console.error('Error loading shedding logs:', err);
      }
    });
  }

  openSheddingModal(): void {
    const now = new Date();
    const localIso = new Date(now.getTime() - now.getTimezoneOffset() * 60000).toISOString().slice(0, 16);
    this.sheddingForm.reset({ sheddingDate: localIso, shedQuality: 'COMPLETE', ateShed: false, notes: '' });
    this.showSheddingModal.set(true);
    this.showActivityMenu.set(false);
  }

  cancelShedding(): void {
    this.showSheddingModal.set(false);
    this.sheddingForm.reset();
  }

  onSubmitShedding(): void {
    if (this.sheddingForm.valid) {
      const reptile = this.reptile();
      if (!reptile) return;

      this.sheddingLoading.set(true);
      const formValue = this.sheddingForm.value;

      const log = {
        reptileId: reptile.id,
        sheddingDate: new Date(formValue.sheddingDate).toISOString(),
        shedQuality: formValue.shedQuality,
        ateShed: formValue.ateShed,
        notes: formValue.notes || null
      };

      this.reptileService.addSheddingLog(log).subscribe({
        next: () => {
          this.sheddingLoading.set(false);
          this.showSheddingModal.set(false);
          this.sheddingForm.reset();
          this.loadSheddingLogs(reptile.id);
        },
        error: (err) => {
          this.sheddingLoading.set(false);
          console.error('Error creating shedding log:', err);
          this.error.set('Failed to create shedding log. Please try again.');
        }
      });
    } else {
      Object.keys(this.sheddingForm.controls).forEach(key => {
        this.sheddingForm.get(key)?.markAsTouched();
      });
    }
  }

  onDeleteSheddingLog(logId: number): void {
    const reptile = this.reptile();
    if (!reptile) return;

    this.reptileService.deleteSheddingLog(logId).subscribe({
      next: () => {
        this.loadSheddingLogs(reptile.id);
      },
      error: (err) => {
        console.error('Error deleting shedding log:', err);
        this.error.set('Failed to delete shedding log. Please try again.');
      }
    });
  }

  // Cleaning log methods
  loadCleaningLogs(enclosureId: number | undefined): void {
    if (!enclosureId) return;

    this.cleaningLogsLoading.set(true);
    this.reptileService.getEnclosureCleaningLogs(enclosureId).subscribe({
      next: (logs) => {
        const sorted = logs.sort((a, b) =>
          new Date(b.cleaningDate).getTime() - new Date(a.cleaningDate).getTime()
        );
        this.cleaningLogs.set(sorted);
        this.cleaningLogsLoading.set(false);
      },
      error: (err) => {
        this.cleaningLogsLoading.set(false);
        console.error('Error loading cleaning logs:', err);
      }
    });
  }

  openCleaningModal(): void {
    const reptile = this.reptile();
    if (!reptile?.enclosureId) {
      this.error.set('This reptile is not assigned to an enclosure. Assign an enclosure first to log cleaning.');
      this.showActivityMenu.set(false);
      return;
    }
    const now = new Date();
    const localIso = new Date(now.getTime() - now.getTimezoneOffset() * 60000).toISOString().slice(0, 16);
    this.cleaningForm.reset({ cleaningDate: localIso, cleaningType: 'SPOT_CLEAN', substrateChanged: false, disinfected: false, notes: '' });
    this.showCleaningModal.set(true);
    this.showActivityMenu.set(false);
  }

  cancelCleaning(): void {
    this.showCleaningModal.set(false);
    this.cleaningForm.reset();
  }

  onSubmitCleaning(): void {
    if (this.cleaningForm.valid) {
      const reptile = this.reptile();
      if (!reptile?.enclosureId) return;

      this.cleaningLoading.set(true);
      const formValue = this.cleaningForm.value;

      const log = {
        enclosureId: reptile.enclosureId,
        cleaningDate: new Date(formValue.cleaningDate).toISOString(),
        cleaningType: formValue.cleaningType,
        substrateChanged: formValue.substrateChanged,
        disinfected: formValue.disinfected,
        notes: formValue.notes || null
      };

      this.reptileService.addEnclosureCleaningLog(log).subscribe({
        next: () => {
          this.cleaningLoading.set(false);
          this.showCleaningModal.set(false);
          this.cleaningForm.reset();
          this.loadCleaningLogs(reptile.enclosureId);
        },
        error: (err) => {
          this.cleaningLoading.set(false);
          console.error('Error creating cleaning log:', err);
          this.error.set('Failed to create cleaning log. Please try again.');
        }
      });
    } else {
      Object.keys(this.cleaningForm.controls).forEach(key => {
        this.cleaningForm.get(key)?.markAsTouched();
      });
    }
  }

  toggleActivityMenu(): void {
    this.showActivityMenu.set(!this.showActivityMenu());
  }

  // Image methods
  openUploadModal(): void {
    this.showUploadModal.set(true);
    this.selectedFile = null;
    this.imageDescription = '';
  }

  cancelUpload(): void {
    this.showUploadModal.set(false);
    this.selectedFile = null;
    this.imageDescription = '';
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  uploadImage(): void {
    const reptile = this.reptile();
    if (!reptile || !this.selectedFile) return;

    this.uploadLoading.set(true);
    this.reptileService.uploadImage(reptile.id, this.selectedFile, this.imageDescription).subscribe({
      next: () => {
        this.uploadLoading.set(false);
        this.showUploadModal.set(false);
        this.selectedFile = null;
        this.imageDescription = '';
        this.loadImages(reptile.id);
      },
      error: (err) => {
        this.uploadLoading.set(false);
        console.error('Error uploading image:', err);
        this.error.set('Failed to upload image. Please try again.');
      }
    });
  }

  deleteImage(imageId: number): void {
    const reptile = this.reptile();
    if (!reptile) return;

    if (confirm('Are you sure you want to delete this image?')) {
      this.reptileService.deleteImage(reptile.id, imageId).subscribe({
        next: () => {
          // If the deleted image was the highlight, update local state
          if (reptile.highlightImageId === imageId) {
            this.reptile.set({ ...reptile, highlightImageId: undefined });
          }
          this.loadImages(reptile.id);
        },
        error: (err) => {
          console.error('Error deleting image:', err);
          this.error.set('Failed to delete image. Please try again.');
        }
      });
    }
  }

  getImageUrl(imageId: number): string {
    const reptile = this.reptile();
    if (!reptile) return '';
    return this.reptileService.getImageUrl(reptile.id, imageId);
  }

  formatFileSize(bytes: number): string {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
  }

  /**
   * Sets an image as the highlight image for this reptile.
   */
  setAsHighlight(imageId: number): void {
    const reptile = this.reptile();
    if (!reptile) return;

    this.reptileService.setHighlightImage(reptile.id, imageId).subscribe({
      next: (updatedReptile) => {
        // Update the local reptile data with new highlightImageId
        this.reptile.set({ ...reptile, highlightImageId: updatedReptile.highlightImageId });
      },
      error: (err) => {
        console.error('Error setting highlight image:', err);
        this.error.set('Failed to set highlight image. Please try again.');
      }
    });
  }

  /**
   * Removes the highlight image designation.
   */
  removeHighlight(): void {
    const reptile = this.reptile();
    if (!reptile || !reptile.highlightImageId) return;

    this.reptileService.removeHighlightImage(reptile.id).subscribe({
      next: () => {
        // Update the local reptile data to remove highlightImageId
        this.reptile.set({ ...reptile, highlightImageId: undefined });
      },
      error: (err) => {
        console.error('Error removing highlight image:', err);
        this.error.set('Failed to remove highlight image. Please try again.');
      }
    });
  }

  /**
   * Checks if an image is the current highlight image.
   */
  isHighlightImage(imageId: number): boolean {
    const reptile = this.reptile();
    return reptile?.highlightImageId === imageId;
  }
}
