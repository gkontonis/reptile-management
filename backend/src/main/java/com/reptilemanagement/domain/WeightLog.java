package com.reptilemanagement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
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
@NoArgsConstructor
@AllArgsConstructor
public class WeightLog {
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

    /** Timestamp when the weight log entry was created */
    private LocalDateTime createdAt;

    /** Timestamp when the weight log entry was last updated */
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