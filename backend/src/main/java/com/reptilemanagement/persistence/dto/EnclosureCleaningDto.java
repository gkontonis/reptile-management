package com.reptilemanagement.persistence.dto;

import com.reptilemanagement.persistence.domain.EnclosureCleaning;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for EnclosureCleaning entity.
 * Used for API communication to decouple the domain model from the API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnclosureCleaningDto {
    /** Unique identifier for the enclosure cleaning entry */
    private Long id;

    /** ID of the enclosure this cleaning log belongs to */
    private Long enclosureId;

    /** Date and time when the cleaning occurred */
    private LocalDateTime cleaningDate;

    /** Type of cleaning performed */
    private EnclosureCleaning.CleaningType cleaningType;

    /** Whether the substrate was changed */
    private Boolean substrateChanged;

    /** Whether the enclosure was disinfected */
    private Boolean disinfected;

    /** Additional notes about the cleaning */
    private String notes;

    /** Timestamp when the enclosure cleaning entry was created */
    private LocalDateTime createdAt;

    /** Timestamp when the enclosure cleaning entry was last updated */
    private LocalDateTime updatedAt;
}