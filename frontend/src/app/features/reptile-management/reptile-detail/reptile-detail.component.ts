import { Component, OnInit, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ReptileDetail, ReptileImage } from '../models/reptile.model';
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

  // Modal states
  showDeleteModal = signal<boolean>(false);
  showEditModal = signal<boolean>(false);
  deleteLoading = signal<boolean>(false);
  editLoading = signal<boolean>(false);

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
    }
  }

  loadReptile(id: number): void {
    this.loading.set(true);
    this.error.set(null);

    this.reptileService.getReptileById(id).subscribe({
      next: (reptile) => {
        this.reptile.set(reptile);
        this.loading.set(false);
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
          // Reload the reptile details
          this.loadReptile(reptile.id);
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
