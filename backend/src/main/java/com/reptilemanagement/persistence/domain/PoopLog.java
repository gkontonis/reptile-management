package com.reptilemanagement.persistence.domain;

import com.reptilemanagement.persistence.domain.base.BaseEntity;
import com.reptilemanagement.persistence.domain.base.EntityUpdatable;
import com.reptilemanagement.persistence.dto.PoopLogDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a poop/defecation log entry for a reptile.
 * Maps to the 'poop_logs' table in the database.
 */
@Entity
@Table(name = "poop_logs")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PoopLog extends BaseEntity<Long> implements EntityUpdatable<PoopLogDto> {

    public enum Consistency {
        NORMAL, RUNNY, HARD, WATERY
    }

    /** Unique identifier for the poop log entry */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID of the reptile this poop log belongs to */
    @Column(nullable = false)
    private Long reptileId;

    /** Date and time when the defecation occurred */
    @Column(nullable = false)
    private LocalDateTime poopDate;

    /** Consistency of the feces */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Consistency consistency;

    /** Color of the feces */
    @Column(length = 100)
    private String color;

    /** Whether parasites or abnormalities were observed */
    @Column(nullable = false)
    private Boolean parasitesPresent = false;

    /** Additional notes about the defecation */
    @Column(length = 500)
    private String notes;

    @Override
    public void update(PoopLogDto dto) {
        if (dto.getReptileId() != null) {
            this.reptileId = dto.getReptileId();
        }
        if (dto.getPoopDate() != null) {
            this.poopDate = dto.getPoopDate();
        }
        if (dto.getConsistency() != null) {
            this.consistency = dto.getConsistency();
        }
        if (dto.getParasitesPresent() != null) {
            this.parasitesPresent = dto.getParasitesPresent();
        }
        this.color = dto.getColor();
        this.notes = dto.getNotes();
    }
}
