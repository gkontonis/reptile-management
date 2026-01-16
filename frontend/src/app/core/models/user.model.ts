/**
 * Basic user information interface.
 */
export interface User {
  /** Unique identifier for the user */
  id: number;
  /** User's login username */
  username: string;
  /** User's email address */
  email: string;
  /** Array of user roles/authorities */
  roles: string[];
}

/**
 * Extended user information with additional metadata.
 */
export interface UserDetail extends User {
  /** Account creation timestamp in ISO string format */
  createdAt: string;
  /** Total number of todos assigned to this user */
  todoCount: number;
}

/**
 * Request payload for creating a new user account.
 */
export interface CreateUserRequest {
  /** Desired username for the new account */
  username: string;
  /** Email address for the new account */
  email: string;
  /** Initial password for the new account */
  password: string;
  /** Roles to assign to the new user */
  roles: string[];
}

/**
 * Request payload for updating an existing user account.
 * All fields are optional - only provided fields will be updated.
 */
export interface UpdateUserRequest {
  /** New email address (optional) */
  email?: string;
  /** New password (optional) */
  password?: string;
  /** New roles array (optional) */
  roles?: string[];
}

/**
 * Request payload for resetting a user's password.
 */
export interface ResetPasswordRequest {
  /** The new password to set */
  newPassword: string;
}
