import { Component, OnInit, signal, inject, computed } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../../core/services/user.service';
import { UserDetail } from '../../../core/models/user.model';
import { ConfirmationDialogComponent } from '../../../shared/components/confirmation-dialog/confirmation-dialog.component';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-user-management',
  imports: [ReactiveFormsModule, ConfirmationDialogComponent, DatePipe],
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private userService = inject(UserService);

  users = signal<UserDetail[]>([]);
  loading = signal(false);
  saving = signal(false);
  successMessage = signal('');
  errorMessage = signal('');

  // Single modal for create + edit
  showUserModal = signal(false);
  editingUser = signal<UserDetail | null>(null);
  isEditMode = computed(() => this.editingUser() !== null);
  modalTitle = computed(() => this.isEditMode() ? `Edit User: ${this.editingUser()!.username}` : 'Create New User');

  // Delete
  showDeleteModal = signal(false);
  deletingUser = signal<UserDetail | null>(null);

  userForm: FormGroup = this.fb.group({
    username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20), Validators.pattern(/^[a-zA-Z0-9_]+$/)]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(8)]],
    roleAdmin: [false],
    roleUser: [true]
  });

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.loading.set(true);
    this.userService.getAllUsers().subscribe({
      next: (users) => { this.users.set(users); this.loading.set(false); },
      error: () => { this.loading.set(false); this.showError('Failed to load users.'); }
    });
  }

  goBack(): void {
    this.router.navigate(['/reptiles']);
  }

  // --- Modal ---

  openCreateModal(): void {
    this.editingUser.set(null);
    this.userForm.reset({ username: '', email: '', password: '', roleAdmin: false, roleUser: true });
    // Username + password required for create
    this.userForm.get('username')!.enable();
    this.userForm.get('password')!.setValidators([Validators.required, Validators.minLength(8)]);
    this.userForm.get('password')!.updateValueAndValidity();
    this.showUserModal.set(true);
  }

  openEditModal(user: UserDetail): void {
    this.editingUser.set(user);
    this.userForm.reset({
      username: user.username,
      email: user.email,
      password: '',
      roleAdmin: user.roles.includes('ROLE_ADMIN'),
      roleUser: user.roles.includes('ROLE_USER')
    });
    // Username not editable, password optional for edit
    this.userForm.get('username')!.disable();
    this.userForm.get('password')!.setValidators([Validators.minLength(8)]);
    this.userForm.get('password')!.updateValueAndValidity();
    this.showUserModal.set(true);
  }

  closeUserModal(): void {
    this.showUserModal.set(false);
    this.editingUser.set(null);
    this.userForm.reset();
  }

  onSubmitUser(): void {
    if (!this.userForm.valid) {
      Object.keys(this.userForm.controls).forEach(k => this.userForm.get(k)?.markAsTouched());
      return;
    }

    const v = this.userForm.getRawValue();
    const roles = [...(v.roleUser ? ['ROLE_USER'] : []), ...(v.roleAdmin ? ['ROLE_ADMIN'] : [])];

    if (roles.length === 0) {
      this.showError('At least one role must be selected.');
      return;
    }

    this.saving.set(true);

    if (this.isEditMode()) {
      const req: Record<string, any> = { email: v.email, roles };
      if (v.password) req['password'] = v.password;
      this.userService.updateUser(this.editingUser()!.id, req).subscribe({
        next: () => this.onSaveSuccess('User updated successfully.'),
        error: (err) => this.onSaveError(err, 'Failed to update user.')
      });
    } else {
      this.userService.createUser({ username: v.username, email: v.email, password: v.password, roles }).subscribe({
        next: () => this.onSaveSuccess('User created successfully.'),
        error: (err) => this.onSaveError(err, 'Failed to create user.')
      });
    }
  }

  // --- Delete ---

  openDeleteModal(user: UserDetail): void {
    this.deletingUser.set(user);
    this.showDeleteModal.set(true);
  }

  confirmDelete(): void {
    const user = this.deletingUser();
    if (!user) return;
    this.userService.deleteUser(user.id).subscribe({
      next: () => {
        this.showDeleteModal.set(false);
        this.deletingUser.set(null);
        this.showSuccess(`User "${user.username}" deleted.`);
        this.loadUsers();
      },
      error: (err) => {
        this.showDeleteModal.set(false);
        this.showError(err.error?.error || err.error?.message || 'Failed to delete user.');
      }
    });
  }

  cancelDelete(): void {
    this.showDeleteModal.set(false);
    this.deletingUser.set(null);
  }

  // --- Helpers ---

  roleLabel(role: string): string {
    return role === 'ROLE_ADMIN' ? 'Admin' : 'User';
  }

  roleBadgeClass(role: string): string {
    return role === 'ROLE_ADMIN' ? 'badge-primary' : 'badge-ghost';
  }

  private onSaveSuccess(message: string): void {
    this.saving.set(false);
    this.closeUserModal();
    this.showSuccess(message);
    this.loadUsers();
  }

  private onSaveError(err: any, fallback: string): void {
    this.saving.set(false);
    this.showError(err.error?.error || err.error?.message || fallback);
  }

  private showSuccess(msg: string): void {
    this.errorMessage.set('');
    this.successMessage.set(msg);
    setTimeout(() => this.successMessage.set(''), 4000);
  }

  private showError(msg: string): void {
    this.successMessage.set('');
    this.errorMessage.set(msg);
    setTimeout(() => this.errorMessage.set(''), 6000);
  }
}
