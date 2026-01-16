package com.reptilemanagement.persistence.domain;

import com.reptilemanagement.persistence.domain.base.BaseEntity;
import com.reptilemanagement.persistence.domain.base.EntityUpdatable;
import com.reptilemanagement.persistence.dto.WeightLogDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity representing a weight measurement log entry for a reptile.
 * Maps to the 'weight_logs' table in the database.
 */
@Entity
@Table(name = "weight_logs")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class WeightLog extends BaseEntity<Long> implements EntityUpdatable<WeightLogDto> {
    /** Unique identifier for the weight log entry */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID of the reptile this weight log belongs to */
    @Column(nullable = false)
    private Long reptileId;

    /** Date and time when the weight was measured */
    @Column(nullable = false)
    private LocalDateTime measurementDate;

    /** Weight of the reptile in grams */
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal weightGrams;

    /** Additional notes about the weight measurement */
    @Column(length = 500)
    private String notes;

    @Override
    public void update(WeightLogDto dto) {
        if (dto.getReptileId() != null) {
            this.reptileId = dto.getReptileId();
        }
        if (dto.getMeasurementDate() != null) {
            this.measurementDate = dto.getMeasurementDate();
        }
        if (dto.getWeightGrams() != null) {
            this.weightGrams = dto.getWeightGrams();
        }
        this.notes = dto.getNotes();
    }
}