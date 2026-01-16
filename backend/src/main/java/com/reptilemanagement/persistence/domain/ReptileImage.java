package com.reptilemanagement.persistence.domain;

import com.reptilemanagement.persistence.domain.base.BaseEntity;
import com.reptilemanagement.persistence.domain.base.EntityUpdatable;
import com.reptilemanagement.persistence.dto.ReptileImageDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity representing an image of a reptile.
 * Images are stored as BLOBs in the database.
 */
@Entity
@Table(name = "reptile_images")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReptileImage extends BaseEntity<Long> implements EntityUpdatable<ReptileImageDto> {
    /** Unique identifier for the image */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID of the reptile this image belongs to */
    @Column(nullable = false)
    private Long reptileId;

    /** Original filename of the uploaded image */
    @Column(nullable = false)
    private String filename;

    /** MIME type of the image (e.g., image/jpeg, image/png) */
    @Column(nullable = false)
    private String contentType;

    /** Binary data of the image stored as a byte array */
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private byte[] imageData;

    /** Optional description of the image */
    @Column(length = 500)
    private String description;

    /** Size of the image in bytes */
    @Column(nullable = false)
    private Long size;

    @Override
    public void update(ReptileImageDto dto) {
        if (dto.getReptileId() != null) {
            this.reptileId = dto.getReptileId();
        }
        if (dto.getFilename() != null) {
            this.filename = dto.getFilename();
        }
        if (dto.getContentType() != null) {
            this.contentType = dto.getContentType();
        }
        this.description = dto.getDescription();
        if (dto.getSize() != null) {
            this.size = dto.getSize();
        }
        // Note: imageData is not updated from DTO as it's excluded from the DTO
    }
}
