package com.reptilemanagement.persistence.domain;

import com.reptilemanagement.persistence.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Entity representing a reptile in the system.
 * Maps to the 'reptiles' table in the database.
 */
@Entity
@Table(name = "reptiles")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Reptile extends BaseEntity<Long> {
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

    /** Enumeration of possible reptile gender values */
    public enum ReptileGender {
        MALE, FEMALE, UNKNOWN
    }

    /** Enumeration of possible reptile status values */
    public enum ReptileStatus {
        ACTIVE, QUARANTINE, DECEASED, SOLD
    }
}