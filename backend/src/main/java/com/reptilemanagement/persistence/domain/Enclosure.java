package com.reptilemanagement.persistence.domain;

import com.reptilemanagement.persistence.domain.base.BaseEntity;
import com.reptilemanagement.persistence.domain.base.EntityUpdatable;
import com.reptilemanagement.persistence.dto.EnclosureDto;
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
public class Enclosure extends BaseEntity<Long> implements EntityUpdatable<EnclosureDto> {
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

    /** ID of the user who owns this enclosure */
    @Column(name = "user_id")
    private Long userId;

    /** Additional notes about the enclosure */
    @Column(length = 1000)
    private String notes;

    /** Enumeration of possible enclosure types */
    public enum EnclosureType {
        TERRARIUM, VIVARIUM, PALUDARIUM, AQUATERRAIUM, CUSTOM
    }

    @Override
    public void update(EnclosureDto dto) {
        if (dto.getName() != null) {
            this.name = dto.getName();
        }
        if (dto.getType() != null) {
            this.type = dto.getType();
        }
        this.dimensions = dto.getDimensions();
        this.substrate = dto.getSubstrate();
        this.heating = dto.getHeating();
        this.lighting = dto.getLighting();
        this.humidity = dto.getHumidity();
        this.temperature = dto.getTemperature();
        this.notes = dto.getNotes();
    }
}