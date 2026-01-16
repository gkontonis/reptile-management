import { Injectable } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

/**
 * Service for sanitizing user input and output to prevent XSS attacks.
 * Provides safe HTML rendering and input validation utilities.
 */
@Injectable({
  providedIn: 'root'
})
export class SanitizationService {

  constructor(private sanitizer: DomSanitizer) {}

  /**
   * Sanitizes HTML content to prevent XSS attacks.
   * Use this when you need to render HTML content from user input.
   *
   * @param html - The HTML string to sanitize
   * @returns SafeHtml that can be safely rendered in templates
   */
  sanitizeHtml(html: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(html);
  }

  /**
   * Sanitizes a URL to prevent XSS attacks.
   * Use this when binding user-provided URLs to template properties.
   *
   * @param url - The URL string to sanitize
   * @returns SafeUrl that can be safely used in templates
   */
  sanitizeUrl(url: string) {
    return this.sanitizer.bypassSecurityTrustUrl(url);
  }

  /**
   * Sanitizes a resource URL (for CSS, JavaScript, etc.) to prevent XSS attacks.
   * Use this when binding user-provided resource URLs.
   *
   * @param url - The resource URL string to sanitize
   * @returns SafeResourceUrl that can be safely used in templates
   */
  sanitizeResourceUrl(url: string) {
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  }

  /**
   * Validates and sanitizes text input by removing potentially dangerous characters.
   * This is a basic input sanitization - for more complex validation, use Angular forms validators.
   *
   * @param input - The input string to sanitize
   * @returns Sanitized string safe for general use
   */
  sanitizeTextInput(input: string): string {
    if (!input) return '';

    // Remove null bytes and other control characters
    return input.replace(/[\x00-\x1F\x7F]/g, '');
  }

  /**
   * Validates email format (basic validation).
   * For comprehensive email validation, use Angular forms validators.
   *
   * @param email - The email string to validate
   * @returns True if email format is valid
   */
  isValidEmailFormat(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  /**
   * Sanitizes a username by removing special characters that could be used in injection attacks.
   *
   * @param username - The username to sanitize
   * @returns Sanitized username
   */
  sanitizeUsername(username: string): string {
    if (!username) return '';

    // Allow only alphanumeric characters, underscores, and hyphens
    return username.replace(/[^a-zA-Z0-9_-]/g, '');
  }
}
