package com.reptilemanagement.persistence.domain;

import com.reptilemanagement.persistence.domain.base.BaseEntity;
import com.reptilemanagement.persistence.domain.base.EntityUpdatable;
import com.reptilemanagement.persistence.dto.ReptileDto;
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
public class Reptile extends BaseEntity<Long> implements EntityUpdatable<ReptileDto> {
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

    /**
     * Updates this entity from a DTO.
     * Only updates business fields, not ID or audit fields.
     *
     * @param dto the DTO containing new values
     */
    @Override
    public void update(ReptileDto dto) {
        if (dto.getName() != null) {
            this.name = dto.getName();
        }
        if (dto.getSpecies() != null) {
            this.species = dto.getSpecies();
        }
        this.subspecies = dto.getSubspecies();
        if (dto.getGender() != null) {
            this.gender = dto.getGender();
        }
        this.birthDate = dto.getBirthDate();
        if (dto.getAcquisitionDate() != null) {
            this.acquisitionDate = dto.getAcquisitionDate();
        }
        this.enclosureId = dto.getEnclosureId();
        if (dto.getStatus() != null) {
            this.status = dto.getStatus();
        }
        this.notes = dto.getNotes();
        this.highlightImageId = dto.getHighlightImageId();
    }
}