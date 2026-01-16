package com.reptilemanagement.persistence.domain;

import com.reptilemanagement.persistence.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a feeding log entry for a reptile.
 * Maps to the 'feeding_logs' table in the database.
 */
@Entity
@Table(name = "feeding_logs")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FeedingLog extends BaseEntity<Long> {
    /** Unique identifier for the feeding log entry */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID of the reptile this feeding log belongs to */
    @Column(nullable = false)
    private Long reptileId;

    /** Date and time when the feeding occurred */
    @Column(nullable = false)
    private LocalDateTime feedingDate;

    /** Type of food fed to the reptile */
    @Column(nullable = false)
    private String foodType;

    /** Quantity of food fed (e.g., "1 mouse", "2 crickets") */
    @Column(nullable = false)
    private String quantity;

    /** Whether the reptile ate the food */
    @Column(nullable = false)
    private Boolean ate = true;

    /** Additional notes about the feeding */
    @Column(length = 500)
    private String notes;
}