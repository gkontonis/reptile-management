package com.reptilemanagement.persistence.domain.base;

import com.reptilemanagement.security.AuthenticationUtils;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Base entity class for domain entities requiring audit fields.
 * <p>
 * Provides automatic auditing for:
 * <ul>
 *   <li>{@code createdAt} - Timestamp when entity was created</li>
 *   <li>{@code createdBy} - User who created the entity</li>
 *   <li>{@code updatedAt} - Timestamp when entity was last modified</li>
 *   <li>{@code updatedBy} - User who last modified the entity</li>
 * </ul>
 * <p>
 * Subclasses must implement {@link #getId()} and {@link #setId(Object)} for the primary key.
 *
 * @param <T> The type of the entity's primary key (e.g., Long, Integer, UUID)
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity<T> implements Identifiable<T> {

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        createdBy = AuthenticationUtils.getAuthenticatedIdentifier();
        updatedBy = createdBy;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        updatedBy = AuthenticationUtils.getAuthenticatedIdentifier();
    }

    /**
     * Returns the primary key of this entity.
     *
     * @return the entity’s primary key
     */
    public abstract T getId();

    /**
     * Sets the primary key of this entity.
     *
     * @param id the primary key to set
     */
    public abstract void setId(T id);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseEntity<?> that = (BaseEntity<?>) o;
        if (getId() == null || that.getId() == null) {
            return false;
        }
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? Objects.hash(getId()) : super.hashCode();
    }
}
