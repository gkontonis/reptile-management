package com.reptilemanagement.persistence.dto;

import com.reptilemanagement.persistence.dto.base.BaseDto;
import com.reptilemanagement.persistence.dto.base.Updatable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for SheddingLog entity.
 * Used for API communication to decouple the domain model from the API.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SheddingLogDto extends BaseDto<Long> implements Updatable<SheddingLogDto> {
    /** Unique identifier for the shedding log entry */
    private Long id;

    /** ID of the reptile this shedding log belongs to */
    private Long reptileId;

    /** Date and time when the shedding was observed */
    private LocalDateTime sheddingDate;

    /** Quality of the shed */
    private String shedQuality;

    /** Whether the reptile ate the shed skin */
    private Boolean ateShed;

    /** Additional notes about the shedding */
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
    public void update(SheddingLogDto dto) {
        if (dto == null) {
            return;
        }
        this.reptileId = dto.getReptileId();
        this.sheddingDate = dto.getSheddingDate();
        this.shedQuality = dto.getShedQuality();
        this.ateShed = dto.getAteShed();
        this.notes = dto.getNotes();
        this.setUpdatedBy(dto.getUpdatedBy());
        this.setUpdatedAt(dto.getUpdatedAt());
    }
}