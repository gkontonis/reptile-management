package com.reptilemanagement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing an enclosure cleaning log entry.
 * Maps to the 'enclosure_cleanings' table in the database.
 */
@Entity
@Table(name = "enclosure_cleanings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnclosureCleaning {
    /** Unique identifier for the enclosure cleaning entry */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID of the enclosure this cleaning log belongs to */
    @Column(nullable = false)
    private Long enclosureId;

    /** Date and time when the cleaning occurred */
    @Column(nullable = false)
    private LocalDateTime cleaningDate;

    /** Type of cleaning performed */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CleaningType cleaningType;

    /** Whether the substrate was changed */
    @Column(nullable = false)
    private Boolean substrateChanged = false;

    /** Whether the enclosure was disinfected */
    @Column(nullable = false)
    private Boolean disinfected = false;

    /** Additional notes about the cleaning */
    @Column(length = 500)
    private String notes;

    /** Timestamp when the enclosure cleaning entry was created */
    private LocalDateTime createdAt;

    /** Timestamp when the enclosure cleaning entry was last updated */
    private LocalDateTime updatedAt;

    /**
     * JPA lifecycle callback executed before persisting the entity.
     * Sets the creation timestamp to current time.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * JPA lifecycle callback executed before updating the entity.
     * Updates the modification timestamp to current time.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /** Enumeration of possible cleaning types */
    public enum CleaningType {
        SPOT_CLEAN, FULL_CLEAN, WATER_CHANGE, DEEP_CLEAN
    }
}