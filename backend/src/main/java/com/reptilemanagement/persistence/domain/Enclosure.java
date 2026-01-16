package com.reptilemanagement.persistence.domain;

import com.reptilemanagement.persistence.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity representing an enclosure for reptiles.
 * Maps to the 'enclosures' table in the database.
 */
@Entity
@Table(name = "enclosures")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Enclosure extends BaseEntity<Long> {
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

    /** Enumeration of possible enclosure types */
    public enum EnclosureType {
        TERRARIUM, VIVARIUM, PALUDARIUM, AQUATERRAIUM, CUSTOM
    }
}