package com.reptilemanagement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a reptile in the system.
 * Maps to the 'reptiles' table in the database.
 */
@Entity
@Table(name = "reptiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reptile {
    /** Unique identifier for the reptile */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the reptile */
    @Column(nullable = false)
    private String name;

    /** Species of the reptile (e.g., "Ball Python", "Bearded Dragon") */
    @Column(nullable = false)
    private String species;

    /** Subspecies or morph (optional) */
    private String subspecies;

    /** Gender of the reptile */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReptileGender gender;

    /** Date of birth (optional) */
    private LocalDate birthDate;

    /** Date when the reptile was acquired */
    @Column(nullable = false)
    private LocalDate acquisitionDate;

    /** ID of the enclosure this reptile is currently in */
    private Long enclosureId;

    /** Current status of the reptile */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReptileStatus status = ReptileStatus.ACTIVE;

    /** Additional notes about the reptile */
    @Column(length = 1000)
    private String notes;

    /** ID of the highlight image for this reptile */
    @Column(name = "highlight_image_id")
    private Long highlightImageId;

    /** Timestamp when the reptile record was created */
    private LocalDateTime createdAt;

    /** Timestamp when the reptile record was last updated */
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

    /** Enumeration of possible reptile gender values */
    public enum ReptileGender {
        MALE, FEMALE, UNKNOWN
    }

    /** Enumeration of possible reptile status values */
    public enum ReptileStatus {
        ACTIVE, QUARANTINE, DECEASED, SOLD
    }
}