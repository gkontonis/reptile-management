package com.reptilemanagement.persistence.domain;

import com.reptilemanagement.persistence.domain.base.BaseEntity;
import com.reptilemanagement.persistence.domain.base.EntityUpdatable;
import com.reptilemanagement.persistence.dto.SheddingLogDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a shedding log entry for a reptile.
 * Maps to the 'shedding_logs' table in the database.
 */
@Entity
@Table(name = "shedding_logs")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SheddingLog extends BaseEntity<Long> implements EntityUpdatable<SheddingLogDto> {
    /** Unique identifier for the shedding log entry */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID of the reptile this shedding log belongs to */
    @Column(nullable = false)
    private Long reptileId;

    /** Date and time when the shedding was observed */
    @Column(nullable = false)
    private LocalDateTime sheddingDate;

    /** Quality of the shed (e.g., "Complete", "Partial", "Incomplete") */
    @Column(nullable = false)
    private String shedQuality;

    /** Whether the reptile ate the shed skin */
    private Boolean ateShed;

    /** Additional notes about the shedding */
    @Column(length = 500)
    private String notes;

    @Override
    public void update(SheddingLogDto dto) {
        if (dto.getReptileId() != null) {
            this.reptileId = dto.getReptileId();
        }
        if (dto.getSheddingDate() != null) {
            this.sheddingDate = dto.getSheddingDate();
        }
        if (dto.getShedQuality() != null) {
            this.shedQuality = dto.getShedQuality();
        }
        this.ateShed = dto.getAteShed();
        this.notes = dto.getNotes();
    }
}