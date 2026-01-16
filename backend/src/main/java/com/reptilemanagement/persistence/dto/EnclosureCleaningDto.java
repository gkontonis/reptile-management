package com.reptilemanagement.persistence.dto;

import com.reptilemanagement.persistence.domain.EnclosureCleaning;
import com.reptilemanagement.persistence.dto.base.BaseDto;
import com.reptilemanagement.persistence.dto.base.Updatable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for EnclosureCleaning entity.
 * Used for API communication to decouple the domain model from the API.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class EnclosureCleaningDto extends BaseDto<Long> implements Updatable<EnclosureCleaningDto> {
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void update(EnclosureCleaningDto dto) {
        if (dto == null) {
            return;
        }
        this.enclosureId = dto.getEnclosureId();
        this.cleaningDate = dto.getCleaningDate();
        this.cleaningType = dto.getCleaningType();
        this.substrateChanged = dto.getSubstrateChanged();
        this.disinfected = dto.getDisinfected();
        this.notes = dto.getNotes();
        this.setUpdatedBy(dto.getUpdatedBy());
        this.setUpdatedAt(dto.getUpdatedAt());
    }
}