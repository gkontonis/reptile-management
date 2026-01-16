package com.reptilemanagement.persistence.domain;

import com.reptilemanagement.persistence.domain.base.BaseEntity;
import com.reptilemanagement.persistence.domain.base.EntityUpdatable;
import com.reptilemanagement.persistence.dto.EnclosureCleaningDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing an enclosure cleaning log entry.
 * Maps to the 'enclosure_cleanings' table in the database.
 */
@Entity
@Table(name = "enclosure_cleanings")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class EnclosureCleaning extends BaseEntity<Long> implements EntityUpdatable<EnclosureCleaningDto> {
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

    /** Enumeration of possible cleaning types */
    public enum CleaningType {
        SPOT_CLEAN, FULL_CLEAN, WATER_CHANGE, DEEP_CLEAN
    }

    @Override
    public void update(EnclosureCleaningDto dto) {
        if (dto.getEnclosureId() != null) {
            this.enclosureId = dto.getEnclosureId();
        }
        if (dto.getCleaningDate() != null) {
            this.cleaningDate = dto.getCleaningDate();
        }
        if (dto.getCleaningType() != null) {
            this.cleaningType = dto.getCleaningType();
        }
        if (dto.getSubstrateChanged() != null) {
            this.substrateChanged = dto.getSubstrateChanged();
        }
        if (dto.getDisinfected() != null) {
            this.disinfected = dto.getDisinfected();
        }
        this.notes = dto.getNotes();
    }
}