package com.reptilemanagement.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for WeightLog entity.
 * Used for API communication to decouple the domain model from the API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeightLogDto {
    /** Unique identifier for the weight log entry */
    private Long id;

    /** ID of the reptile this weight log belongs to */
    private Long reptileId;

    /** Date and time when the weight was measured */
    private LocalDateTime measurementDate;

    /** Weight of the reptile in grams */
    private BigDecimal weightGrams;

    /** Additional notes about the weight measurement */
    private String notes;

    /** Timestamp when the weight log entry was created */
    private LocalDateTime createdAt;

    /** Timestamp when the weight log entry was last updated */
    private LocalDateTime updatedAt;
}