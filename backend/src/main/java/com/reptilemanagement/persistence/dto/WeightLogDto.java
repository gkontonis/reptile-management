package com.reptilemanagement.persistence.dto;

import com.reptilemanagement.persistence.dto.base.BaseDto;
import com.reptilemanagement.persistence.dto.base.Updatable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for WeightLog entity.
 * Used for API communication to decouple the domain model from the API.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class WeightLogDto extends BaseDto<Long> implements Updatable<WeightLogDto> {
    /** Unique identifier for the weight log entry */
    private Long id;

    /** ID of the reptile this weight log belongs to */
    private Long reptileId;

    /** Date and time when the weight was measured */
    private LocalDateTime measurementDate;

    /** Weight of the reptile in grams */
    private BigDecimal weightGrams;

    /** Additional notes about the weight measurement */
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
    public void update(WeightLogDto dto) {
        if (dto == null) {
            return;
        }
        this.reptileId = dto.getReptileId();
        this.measurementDate = dto.getMeasurementDate();
        this.weightGrams = dto.getWeightGrams();
        this.notes = dto.getNotes();
        this.setUpdatedBy(dto.getUpdatedBy());
        this.setUpdatedAt(dto.getUpdatedAt());
    }
}