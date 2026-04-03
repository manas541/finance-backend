package com.zorvyn.finance_backend.Controller;


import com.zorvyn.finance_backend.entity.User;
import com.zorvyn.finance_backend.Service.UserService;
import com.zorvyn.finance_backend.DTO.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * UserController - REST API endpoints for users
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * GET /api/v1/users
     * Get all users
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserDTO> dtos = userService.convertToDTOs(users);
        return ResponseEntity.ok(dtos);
    }

    /**
     * GET /api/v1/users/{id}
     * Get user by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID " + id + " not found");
        }

        UserDTO dto = userService.convertToDTO(user.get());
        return ResponseEntity.ok(dto);
    }

    /**
     * GET /api/v1/users/email/{email}
     * Get user by email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with email '" + email + "' not found");
        }

        UserDTO dto = userService.convertToDTO(user.get());
        return ResponseEntity.ok(dto);
    }

    /**
     * GET /api/v1/users/active/all
     * Get all active users
     */
    @GetMapping("/active/all")
    public ResponseEntity<List<UserDTO>> getActiveUsers() {
        List<User> users = userService.getActiveUsers();
        List<UserDTO> dtos = userService.convertToDTOs(users);
        return ResponseEntity.ok(dtos);
    }

    /**
     * POST /api/v1/users
     * Create new user
     *
     * Request body:
     * {
     *     "name": "John Doe",
     *     "email": "john@example.com",
     *     "password": "password123",
     *     "role": {"id": 1}
     * }
     */
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            User createdUser = userService.createUser(
                    user.getName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRole().getId()
            );

            UserDTO dto = userService.convertToDTO(createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error creating user: " + e.getMessage());
        }
    }

    /**
     * PUT /api/v1/users/{id}
     * Update user
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(
                    id,
                    user.getName(),
                    user.getRole() != null ? user.getRole().getId() : null,
                    user.getIsActive()
            );

            UserDTO dto = userService.convertToDTO(updatedUser);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating user: " + e.getMessage());
        }
    }

    /**
     * DELETE /api/v1/users/{id}
     * Soft delete user
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.softDeleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error deleting user: " + e.getMessage());
        }
    }

    /**
     * POST /api/v1/users/{id}/activate
     * Activate user
     */
    @PostMapping("/{id}/activate")
    public ResponseEntity<?> activateUser(@PathVariable Long id) {
        try {
            User user = userService.toggleUserActive(id, true);
            UserDTO dto = userService.convertToDTO(user);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * POST /api/v1/users/{id}/deactivate
     * Deactivate user
     */
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        try {
            User user = userService.toggleUserActive(id, false);
            UserDTO dto = userService.convertToDTO(user);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }
}