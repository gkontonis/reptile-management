package com.reptilemanagement.dto;

import com.reptilemanagement.domain.Enclosure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for Enclosure entity.
 * Used for API communication to decouple the domain model from the API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnclosureDto {
    /** Unique identifier for the enclosure */
    private Long id;

    /** Name of the enclosure */
    private String name;

    /** Type of enclosure */
    private Enclosure.EnclosureType type;

    /** Dimensions of the enclosure */
    private String dimensions;

    /** Substrate used in the enclosure */
    private String substrate;

    /** Heating method used */
    private String heating;

    /** Lighting setup */
    private String lighting;

    /** Humidity level maintained */
    private String humidity;

    /** Temperature range maintained */
    private String temperature;

    /** Additional notes about the enclosure */
    private String notes;

    /** Timestamp when the enclosure record was created */
    private LocalDateTime createdAt;

    /** Timestamp when the enclosure record was last updated */
    private LocalDateTime updatedAt;
}