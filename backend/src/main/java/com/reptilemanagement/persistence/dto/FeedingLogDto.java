package com.reptilemanagement.persistence.dto;

import com.reptilemanagement.persistence.dto.base.BaseDto;
import com.reptilemanagement.persistence.dto.base.Updatable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for FeedingLog entity.
 * Used for API communication to decouple the domain model from the API.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FeedingLogDto extends BaseDto<Long> implements Updatable<FeedingLogDto> {
    /** Unique identifier for the feeding log entry */
    private Long id;

    /** ID of the reptile this feeding log belongs to */
    private Long reptileId;

    /** Date and time when the feeding occurred */
    private LocalDateTime feedingDate;

    /** Type of food fed to the reptile */
    private String foodType;

    /** Quantity of food fed */
    private String quantity;

    /** Whether the reptile ate the food */
    private Boolean ate;

    /** Additional notes about the feeding */
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
    public void update(FeedingLogDto dto) {
        if (dto == null) {
            return;
        }
        this.reptileId = dto.getReptileId();
        this.feedingDate = dto.getFeedingDate();
        this.foodType = dto.getFoodType();
        this.quantity = dto.getQuantity();
        this.ate = dto.getAte();
        this.notes = dto.getNotes();
        this.setUpdatedBy(dto.getUpdatedBy());
        this.setUpdatedAt(dto.getUpdatedAt());
    }
}