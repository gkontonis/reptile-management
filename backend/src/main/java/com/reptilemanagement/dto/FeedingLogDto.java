package com.reptilemanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for FeedingLog entity.
 * Used for API communication to decouple the domain model from the API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedingLogDto {
    /** Unique identifier for the feeding log entry */
    private Long id;

    /** ID of the reptile this feeding log belongs to */
    private Long reptileId;

    /** Date and time when the feeding occurred */
    private LocalDateTime feedingDate;

    /** Type of food fed to the reptile */
    private String foodType;

    /** Quantity of food fed */
    private String quantity;

    /** Whether the reptile ate the food */
    private Boolean ate;

    /** Additional notes about the feeding */
    private String notes;

    /** Timestamp when the feeding log entry was created */
    private LocalDateTime createdAt;

    /** Timestamp when the feeding log entry was last updated */
    private LocalDateTime updatedAt;
}