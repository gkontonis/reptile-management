import { Component, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { ReptileService } from '../services/reptile.service';
import { Reptile } from '../models/reptile.model';

@Component({
  selector: 'app-add-reptile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './add-reptile.component.html',
  styleUrls: ['./add-reptile.component.css']
})
export class AddReptileComponent {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private reptileService = inject(ReptileService);

  loading = signal<boolean>(false);
  error = signal<string | null>(null);

  reptileForm: FormGroup = this.fb.group({
    name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    species: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
    subspecies: ['', Validators.maxLength(100)],
    gender: ['UNKNOWN', Validators.required],
    status: ['ACTIVE', Validators.required],
    acquisitionDate: ['', Validators.required],
    birthDate: [''],
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

  onSubmit(): void {
    if (this.reptileForm.valid) {
      this.loading.set(true);
      this.error.set(null);

      const formValue = this.reptileForm.value;

      // Convert date strings to proper format
      const reptileData: Partial<Reptile> = {
        ...formValue,
        acquisitionDate: formValue.acquisitionDate ? new Date(formValue.acquisitionDate).toISOString().split('T')[0] : undefined,
        birthDate: formValue.birthDate ? new Date(formValue.birthDate).toISOString().split('T')[0] : undefined
      };

      this.reptileService.createReptile(reptileData as Omit<Reptile, 'id' | 'createdAt' | 'updatedAt'>)
        .subscribe({
          next: (reptile) => {
            this.loading.set(false);
            this.router.navigate(['/reptiles']);
          },
          error: (err) => {
            this.loading.set(false);
            this.error.set('Failed to create reptile. Please try again.');
            console.error('Error creating reptile:', err);
          }
        });
    } else {
      // Mark all fields as touched to show validation errors
      Object.keys(this.reptileForm.controls).forEach(key => {
        this.reptileForm.get(key)?.markAsTouched();
      });
    }
  }

  onCancel(): void {
    this.router.navigate(['/reptiles']);
  }

  getFieldError(fieldName: string): string | null {
    const field = this.reptileForm.get(fieldName);
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
    }
    return null;
  }
}