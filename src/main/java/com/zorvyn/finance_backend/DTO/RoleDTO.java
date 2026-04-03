package com.zorvyn.finance_backend.DTO;


import lombok.*;

/**
 * RoleDTO - Data Transfer Object for roles
 *
 * Why DTOs?
 * - Entity might have sensitive data
 * - API shouldn't expose everything
 * - Control what client receives
 *
 * Example:
 * Entity User has: password
 * DTO UserDTO has: NO password
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO {
    private Long id;
    private String name;
    private String description;
    private String roleType;
}
