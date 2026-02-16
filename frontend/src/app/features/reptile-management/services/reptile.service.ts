import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reptile, ReptileDetail, FeedingLog, WeightLog, SheddingLog, PoopLog, EnclosureCleaning, Enclosure, ReptileImage } from '../models/reptile.model';

@Injectable({
  providedIn: 'root'
})
export class ReptileService {
  private apiUrl = '/api/reptiles';

  constructor(private http: HttpClient) {}

  // Reptile CRUD operations
  getAllReptiles(): Observable<Reptile[]> {
    return this.http.get<Reptile[]>(this.apiUrl);
  }

  getReptileById(id: number): Observable<ReptileDetail> {
    return this.http.get<ReptileDetail>(`${this.apiUrl}/${id}`);
  }

  createReptile(reptile: Omit<ReptileDetail, 'id' | 'createdAt' | 'updatedAt'>): Observable<Reptile> {
    return this.http.post<Reptile>(this.apiUrl, reptile);
  }

  updateReptile(id: number, reptile: Partial<ReptileDetail>): Observable<Reptile> {
    return this.http.put<Reptile>(`${this.apiUrl}/${id}`, reptile);
  }

  deleteReptile(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Feeding logs
  private feedingLogUrl = '/api/feeding-logs';

  getFeedingLogs(reptileId: number): Observable<FeedingLog[]> {
    return this.http.get<FeedingLog[]>(`${this.feedingLogUrl}/reptile/${reptileId}`);
  }

  addFeedingLog(log: Omit<FeedingLog, 'id'>): Observable<FeedingLog> {
    return this.http.post<FeedingLog>(this.feedingLogUrl, log);
  }

  updateFeedingLog(id: number, log: FeedingLog): Observable<FeedingLog> {
    return this.http.put<FeedingLog>(`${this.feedingLogUrl}/${id}`, log);
  }

  deleteFeedingLog(id: number): Observable<void> {
    return this.http.delete<void>(`${this.feedingLogUrl}/${id}`);
  }

  // Weight logs
  private weightLogUrl = '/api/weight-logs';

  getWeightLogs(reptileId: number): Observable<WeightLog[]> {
    return this.http.get<WeightLog[]>(`${this.weightLogUrl}/reptile/${reptileId}`);
  }

  addWeightLog(log: Omit<WeightLog, 'id'>): Observable<WeightLog> {
    return this.http.post<WeightLog>(this.weightLogUrl, log);
  }

  // Shedding logs
  private sheddingLogUrl = '/api/shedding-logs';

  getSheddingLogs(reptileId: number): Observable<SheddingLog[]> {
    return this.http.get<SheddingLog[]>(`${this.sheddingLogUrl}/reptile/${reptileId}`);
  }

  addSheddingLog(log: Omit<SheddingLog, 'id'>): Observable<SheddingLog> {
    return this.http.post<SheddingLog>(this.sheddingLogUrl, log);
  }

  updateSheddingLog(id: number, log: SheddingLog): Observable<SheddingLog> {
    return this.http.put<SheddingLog>(`${this.sheddingLogUrl}/${id}`, log);
  }

  deleteSheddingLog(id: number): Observable<void> {
    return this.http.delete<void>(`${this.sheddingLogUrl}/${id}`);
  }

  // Poop logs
  private poopLogUrl = '/api/poop-logs';

  getPoopLogs(reptileId: number): Observable<PoopLog[]> {
    return this.http.get<PoopLog[]>(`${this.poopLogUrl}/reptile/${reptileId}`);
  }

  addPoopLog(log: Omit<PoopLog, 'id'>): Observable<PoopLog> {
    return this.http.post<PoopLog>(this.poopLogUrl, log);
  }

  updatePoopLog(id: number, log: PoopLog): Observable<PoopLog> {
    return this.http.put<PoopLog>(`${this.poopLogUrl}/${id}`, log);
  }

  deletePoopLog(id: number): Observable<void> {
    return this.http.delete<void>(`${this.poopLogUrl}/${id}`);
  }

  // Enclosure operations
  getAllEnclosures(): Observable<Enclosure[]> {
    return this.http.get<Enclosure[]>('/api/enclosures');
  }

  getEnclosureById(id: number): Observable<Enclosure> {
    return this.http.get<Enclosure>(`/api/enclosures/${id}`);
  }

  // Enclosure cleaning logs
  private cleaningLogUrl = '/api/enclosure-cleanings';

  getEnclosureCleaningLogs(enclosureId: number): Observable<EnclosureCleaning[]> {
    return this.http.get<EnclosureCleaning[]>(`${this.cleaningLogUrl}/enclosure/${enclosureId}`);
  }

  addEnclosureCleaningLog(log: Omit<EnclosureCleaning, 'id'>): Observable<EnclosureCleaning> {
    return this.http.post<EnclosureCleaning>(this.cleaningLogUrl, log);
  }

  // Dashboard statistics
  getReptileStats(): Observable<{
    totalReptiles: number;
    activeReptiles: number;
    needsFeeding: number;
    needsCleaning: number;
  }> {
    return this.http.get<{
      totalReptiles: number;
      activeReptiles: number;
      needsFeeding: number;
      needsCleaning: number;
    }>(`${this.apiUrl}/stats`);
  }

  // Image operations
  
  /**
   * Uploads an image for a reptile.
   * @param reptileId the reptile ID
   * @param file the image file to upload
   * @param description optional description for the image
   * @returns the created image metadata
   */
  uploadImage(reptileId: number, file: File, description?: string): Observable<ReptileImage> {
    const formData = new FormData();
    formData.append('file', file);
    if (description) {
      formData.append('description', description);
    }
    return this.http.post<ReptileImage>(`${this.apiUrl}/${reptileId}/images`, formData);
  }

  /**
   * Retrieves all image metadata for a reptile.
   * @param reptileId the reptile ID
   * @returns list of image metadata
   */
  getImages(reptileId: number): Observable<ReptileImage[]> {
    return this.http.get<ReptileImage[]>(`${this.apiUrl}/${reptileId}/images`);
  }

  /**
   * Gets the URL for downloading/displaying an image.
   * @param reptileId the reptile ID
   * @param imageId the image ID
   * @returns the image URL
   */
  getImageUrl(reptileId: number, imageId: number): string {
    return `${this.apiUrl}/${reptileId}/images/${imageId}`;
  }

  /**
   * Deletes an image by ID.
   * @param reptileId the reptile ID
   * @param imageId the image ID
   * @returns void observable
   */
  deleteImage(reptileId: number, imageId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${reptileId}/images/${imageId}`);
  }

  /**
   * Sets the highlight image for a reptile.
   * @param reptileId the reptile ID
   * @param imageId the image ID to set as highlight
   * @returns the updated reptile
   */
  setHighlightImage(reptileId: number, imageId: number): Observable<Reptile> {
    return this.http.patch<Reptile>(
      `${this.apiUrl}/${reptileId}/highlight-image`,
      null,
      { params: { imageId: imageId.toString() } }
    );
  }

  /**
   * Removes the highlight image from a reptile.
   * @param reptileId the reptile ID
   * @returns void observable
   */
  removeHighlightImage(reptileId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${reptileId}/highlight-image`);
  }
}