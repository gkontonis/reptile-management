package com.reptilemanagement.persistence.dto;

import com.reptilemanagement.persistence.domain.Enclosure;
import com.reptilemanagement.persistence.dto.base.BaseDto;
import com.reptilemanagement.persistence.dto.base.Updatable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Enclosure entity.
 * Used for API communication to decouple the domain model from the API.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class EnclosureDto extends BaseDto<Long> implements Updatable<EnclosureDto> {
    /** Unique identifier for the enclosure */
    private Long id;

    /** Name of the enclosure */
    private String name;

    /** Type of enclosure */
    private Enclosure.EnclosureType type;

    /** Dimensions of the enclosure */
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
    public void update(EnclosureDto dto) {
        if (dto == null) {
            return;
        }
        this.name = dto.getName();
        this.type = dto.getType();
        this.dimensions = dto.getDimensions();
        this.substrate = dto.getSubstrate();
        this.heating = dto.getHeating();
        this.lighting = dto.getLighting();
        this.humidity = dto.getHumidity();
        this.temperature = dto.getTemperature();
        this.notes = dto.getNotes();
        this.setUpdatedBy(dto.getUpdatedBy());
        this.setUpdatedAt(dto.getUpdatedAt());
    }
}