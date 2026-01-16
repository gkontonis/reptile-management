/**
 * Request payload for user authentication.
 */
export interface AuthRequest {
  /** User's login username */
  username: string;
  /** User's login password */
  password: string;
}

/**
 * Response payload from successful authentication.
 */
export interface AuthResponse {
  /** JWT token for authenticated requests */
  token: string;
  /** Authenticated user's username */
  username: string;
  /** Array of user roles/authorities */
  roles: string[];
}
