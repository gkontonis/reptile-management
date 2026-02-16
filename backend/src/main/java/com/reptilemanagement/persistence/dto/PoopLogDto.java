package com.reptilemanagement.persistence.dto;

import com.reptilemanagement.persistence.domain.PoopLog;
import com.reptilemanagement.persistence.dto.base.BaseDto;
import com.reptilemanagement.persistence.dto.base.Updatable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for PoopLog entity.
 * Used for API communication to decouple the domain model from the API.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PoopLogDto extends BaseDto<Long> implements Updatable<PoopLogDto> {
    /** Unique identifier for the poop log entry */
    private Long id;

    /** ID of the reptile this poop log belongs to */
    private Long reptileId;

    /** Date and time when the defecation occurred */
    private LocalDateTime poopDate;

    /** Consistency of the feces */
    private PoopLog.Consistency consistency;

    /** Color of the feces */
    private String color;

    /** Whether parasites or abnormalities were observed */
    private Boolean parasitesPresent;

    /** Additional notes about the defecation */
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
    public void update(PoopLogDto dto) {
        if (dto == null) {
            return;
        }
        this.reptileId = dto.getReptileId();
        this.poopDate = dto.getPoopDate();
        this.consistency = dto.getConsistency();
        this.color = dto.getColor();
        this.parasitesPresent = dto.getParasitesPresent();
        this.notes = dto.getNotes();
        this.setUpdatedBy(dto.getUpdatedBy());
        this.setUpdatedAt(dto.getUpdatedAt());
    }
}
