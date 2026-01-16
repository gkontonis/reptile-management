package com.reptilemanagement.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entity representing an enclosure for reptiles.
 * Maps to the 'enclosures' table in the database.
 */
@Entity
@Table(name = "enclosures")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enclosure {
    /** Unique identifier for the enclosure */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Name of the enclosure */
    @Column(nullable = false)
    private String name;

    /** Type of enclosure */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnclosureType type;

    /** Dimensions of the enclosure (e.g., "24x18x18 inches") */
    private String dimensions;

    /** Substrate used in the enclosure */
    private String substrate;

    /** Heating method used */
    private String heating;

    /** Lighting setup */
    private String lighting;

    /** Humidity level maintained */
    private String humidity;

    /** Temperature range maintained */
    private String temperature;

    /** Additional notes about the enclosure */
    @Column(length = 1000)
    private String notes;

    /** Timestamp when the enclosure record was created */
    private LocalDateTime createdAt;

    /** Timestamp when the enclosure record was last updated */
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

    /** Enumeration of possible enclosure types */
    public enum EnclosureType {
        TERRARIUM, VIVARIUM, PALUDARIUM, AQUATERRAIUM, CUSTOM
    }
}