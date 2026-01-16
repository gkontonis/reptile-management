package com.reptilemanagement.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for SheddingLog entity.
 * Used for API communication to decouple the domain model from the API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SheddingLogDto {
    /** Unique identifier for the shedding log entry */
    private Long id;

    /** ID of the reptile this shedding log belongs to */
    private Long reptileId;

    /** Date and time when the shedding was observed */
    private LocalDateTime sheddingDate;

    /** Quality of the shed */
    private String shedQuality;

    /** Whether the reptile ate the shed skin */
    private Boolean ateShed;

    /** Additional notes about the shedding */
    private String notes;

    /** Timestamp when the shedding log entry was created */
    private LocalDateTime createdAt;

    /** Timestamp when the shedding log entry was last updated */
    private LocalDateTime updatedAt;
}