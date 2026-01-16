package com.reptilemanagement.persistence.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Entity representing a user in the system.
 * Maps to the 'users' table in the database.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /** Unique identifier for the user */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Unique username for authentication */
    @Column(unique = true, nullable = false)
    private String username;

    /** Hashed password for authentication */
    @Column(nullable = false)
    private String password;

    /** User's email address */
    @Column(nullable = false)
    private String email;

    /** Set of roles assigned to the user for authorization */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles;

    /** User who created this account */
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    /** User who last updated this account */
    @Column(name = "updated_by")
    private String updatedBy;

    /** Timestamp when the user account was created */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Timestamp when the user account was last updated */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * JPA lifecycle callback executed before persisting the entity.
     * Sets the creation timestamp and audit fields.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        // For User entity, we don't use AuthenticationUtils since the user might not be authenticated during creation
        if (createdBy == null) {
            createdBy = "system";
        }
        if (updatedBy == null) {
            updatedBy = createdBy;
        }
    }

    /**
     * JPA lifecycle callback executed before updating the entity.
     * Updates the modification timestamp and audit fields.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        // Set updatedBy during update, defaulting to "system" if not set
        if (updatedBy == null) {
            updatedBy = "system";
        }
    }
}