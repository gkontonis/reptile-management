import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PerformanceService {

  constructor() { }

  /**
   * Marks a performance point
   * @param name The name of the mark
   */
  mark(name: string): void {
    if (typeof performance !== 'undefined' && performance.mark) {
      performance.mark(name);
    }
  }

  /**
   * Measures the time between two marks
   * @param name The name of the measure
   * @param startMark The start mark name
   * @param endMark The end mark name
   */
  measure(name: string, startMark: string, endMark: string): void {
    if (typeof performance !== 'undefined' && performance.measure) {
      try {
        performance.measure(name, startMark, endMark);
      } catch (e) {
        console.warn('Performance measure failed:', e);
      }
    }
  }

  /**
   * Gets the measure duration
   * @param name The measure name
   * @returns The duration in milliseconds
   */
  getMeasureDuration(name: string): number | null {
    if (typeof performance !== 'undefined' && performance.getEntriesByName) {
      const entry = performance.getEntriesByName(name)[0];
      return entry ? entry.duration : null;
    }
    return null;
  }

  /**
   * Logs performance info
   * @param message The message to log
   * @param duration The duration
   */
  logPerformance(message: string, duration: number): void {
    console.log(`[Performance] ${message}: ${duration.toFixed(2)}ms`);
  }
}
