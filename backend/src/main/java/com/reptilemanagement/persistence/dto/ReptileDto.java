package com.reptilemanagement.persistence.dto;

import com.reptilemanagement.persistence.domain.Reptile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Reptile entity.
 * Used for API communication to decouple the domain model from the API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReptileDto {
    /** Unique identifier for the reptile */
    private Long id;

    /** Name of the reptile */
    private String name;

    /** Species of the reptile */
    private String species;

    /** Subspecies or morph */
    private String subspecies;

    /** Gender of the reptile */
    private Reptile.ReptileGender gender;

    /** Date of birth */
    private LocalDate birthDate;

    /** Date when the reptile was acquired */
    private LocalDate acquisitionDate;

    /** ID of the enclosure this reptile is currently in */
    private Long enclosureId;

    /** Current status of the reptile */
    private Reptile.ReptileStatus status;

    /** Additional notes about the reptile */
    private String notes;

    /** ID of the highlight image for this reptile */
    private Long highlightImageId;

    /** Timestamp when the reptile record was created */
    private LocalDateTime createdAt;

    /** Timestamp when the reptile record was last updated */
    private LocalDateTime updatedAt;
}