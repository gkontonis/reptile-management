package com.reptilemanagement.persistence.dto;

import com.reptilemanagement.persistence.dto.base.BaseDto;
import com.reptilemanagement.persistence.dto.base.Updatable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for ReptileImage entity.
 * Excludes the binary image data for lightweight responses.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReptileImageDto extends BaseDto<Long> implements Updatable<ReptileImageDto> {
    /** Unique identifier for the image */
    private Long id;

    /** ID of the reptile this image belongs to */
    private Long reptileId;

    /** Original filename of the uploaded image */
    private String filename;

    /** MIME type of the image */
    private String contentType;

    /** Optional description of the image */
    private String description;

    /** Size of the image in bytes */
    private Long size;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public void update(ReptileImageDto dto) {
        if (dto == null) {
            return;
        }
        this.reptileId = dto.getReptileId();
        this.filename = dto.getFilename();
        this.contentType = dto.getContentType();
        this.description = dto.getDescription();
        this.size = dto.getSize();
        this.setUpdatedBy(dto.getUpdatedBy());
        this.setUpdatedAt(dto.getUpdatedAt());
    }
}
