package com.zorvyn.finance_backend.DTO;

import lombok.*;
import java.time.LocalDateTime;

/**
 * UserDTO - Safe to return to API client
 * NO PASSWORD! (Security!)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private RoleDTO role;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // Notice: NO password field!
}