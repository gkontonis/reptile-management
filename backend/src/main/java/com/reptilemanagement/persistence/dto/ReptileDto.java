package com.reptilemanagement.persistence.dto;

import com.reptilemanagement.persistence.domain.Reptile;
import com.reptilemanagement.persistence.dto.base.BaseDto;
import com.reptilemanagement.persistence.dto.base.Updatable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for Reptile entity.
 * Used for API communication to decouple the domain model from the API.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReptileDto extends BaseDto<Long> implements Updatable<ReptileDto> {
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

    /** ID of the user who owns this reptile */
    private Long userId;

    /** Additional notes about the reptile */
    private String notes;

    /** ID of the highlight image for this reptile */
    private Long highlightImageId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void update(ReptileDto dto) {
        if (dto == null) {
            return;
        }
        this.name = dto.getName();
        this.species = dto.getSpecies();
        this.subspecies = dto.getSubspecies();
        this.gender = dto.getGender();
        this.birthDate = dto.getBirthDate();
        this.acquisitionDate = dto.getAcquisitionDate();
        this.enclosureId = dto.getEnclosureId();
        this.status = dto.getStatus();
        this.notes = dto.getNotes();
        this.highlightImageId = dto.getHighlightImageId();
        this.setUpdatedBy(dto.getUpdatedBy());
        this.setUpdatedAt(dto.getUpdatedAt());
    }
}