package com.zorvyn.finance_backend.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Role Entity - User roles in system
 * @Entity: This is a database table
 * @Table(name = "roles"): Table name is "roles"
 */
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    /**
     * @Id: Primary key
     * @GeneratedValue: Auto-increment (1, 2, 3...)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Role name
     * nullable = false: Must have value
     * unique = true: Cannot duplicate
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * Description of role
     */
    @Column(length = 500)
    private String description;

    /**
     * Role type enum
     * @Enumerated(EnumType.STRING): Store as text in database
     * Values: VIEWER, ANALYST, ADMIN
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType roleType;

    /**
     * Enum - Type-safe (no string typos!)
     */
    public enum RoleType {
        VIEWER,    // Can only view
        ANALYST,   // Can create records
        ADMIN      // Can do everything
    }
}