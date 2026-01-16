package com.reptilemanagement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing a shedding log entry for a reptile.
 * Maps to the 'shedding_logs' table in the database.
 */
@Entity
@Table(name = "shedding_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SheddingLog {
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

    /** Timestamp when the shedding log entry was created */
    private LocalDateTime createdAt;

    /** Timestamp when the shedding log entry was last updated */
    private LocalDateTime updatedAt;

    /**
     * JPA lifecycle callback executed before persisting the entity.
     * Sets the creation timestamp to current time.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * JPA lifecycle callback executed before updating the entity.
     * Updates the modification timestamp to current time.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}