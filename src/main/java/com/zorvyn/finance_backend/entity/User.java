package com.zorvyn.finance_backend.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * User Entity - Users in system
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User's name
     * length=100: Max 100 characters
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * User's email
     * unique=true: No duplicate emails
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * User's password
     * Note: In real app, HASH this! (bcrypt)
     */
    @Column(nullable = false)
    private String password;

    /**
     * User's role
     * @ManyToOne: Many users have ONE role
     * @JoinColumn: Creates foreign key
     */
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /**
     * Is user active?
     * true = can use system
     * false = disabled
     */
    @Column(nullable = false)
    private Boolean isActive = true;

    /**
     * When was user created?
     * updatable=false: Cannot change after creation
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * When was user last updated?
     */
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Soft delete
     * Instead of deleting, mark as deleted
     * Keeps data for auditing
     */
    @Column(nullable = false)
    private Boolean isDeleted = false;

    /**
     * @PrePersist: Runs BEFORE saving (first time)
     * Sets timestamps automatically
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * @PreUpdate: Runs BEFORE updating
     * Updates the "updated" timestamp
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}