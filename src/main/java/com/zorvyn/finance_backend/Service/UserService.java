package com.zorvyn.finance_backend.Service;


import com.zorvyn.finance_backend.entity.User;
import com.zorvyn.finance_backend.entity.Role;
import com.zorvyn.finance_backend.Repository.UserRepository;
import com.zorvyn.finance_backend.Repository.RoleRepository;
import com.zorvyn.finance_backend.DTO.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * UserService - Business logic for users
 */
@Service
@RequiredArgsConstructor
public class UserService {

    /**
     * Both injected automatically by Spring
     */
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    /**
     * Get all active (non-deleted) users
     */
    public List<User> getAllUsers() {
        return userRepository.findByIsDeletedFalse();
    }

    /**
     * Get user by ID
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id)
                .filter(user -> !user.getIsDeleted());
    }

    /**
     * Get user by email
     */
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmailAndIsDeletedFalse(email);
    }

    /**
     * Get all users with specific role
     */
    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRoleAndIsDeletedFalse(role);
    }

    /**
     * Get all active users (enabled)
     */
    public List<User> getActiveUsers() {
        return userRepository.findByIsDeletedFalseAndIsActiveTrue();
    }

    /**
     * Create new user
     *
     * Validation:
     * 1. Email must not exist
     * 2. Name must not be empty
     * 3. Password must not be empty
     * 4. Role must exist
     */
    public User createUser(String name, String email, String password, Long roleId) throws Exception {
        // Check email exists
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isPresent()) {
            throw new Exception("User with email '" + email + "' already exists!");
        }

        // Check name
        if (name == null || name.trim().isEmpty()) {
            throw new Exception("User name cannot be empty!");
        }

        // Check password
        if (password == null || password.trim().isEmpty()) {
            throw new Exception("Password cannot be empty!");
        }

        // Find role
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isEmpty()) {
            throw new Exception("Role with ID " + roleId + " not found!");
        }

        // Create user
        User user = User.builder()
                .name(name)
                .email(email)
                .password(password)  // In production: HASH this with bcrypt!
                .role(role.get())
                .isActive(true)
                .isDeleted(false)
                .build();

        // Save and return
        return userRepository.save(user);
    }

    /**
     * Update user
     */
    public User updateUser(Long id, String name, Long roleId, Boolean isActive) throws Exception {
        // Find user
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new Exception("User with ID " + id + " not found!");
        }

        User user = userOptional.get();

        // Update name
        if (name != null && !name.trim().isEmpty()) {
            user.setName(name);
        }

        // Update role
        if (roleId != null) {
            Optional<Role> role = roleRepository.findById(roleId);
            if (role.isEmpty()) {
                throw new Exception("Role with ID " + roleId + " not found!");
            }
            user.setRole(role.get());
        }

        // Update active status
        if (isActive != null) {
            user.setIsActive(isActive);
        }

        // Save and return
        return userRepository.save(user);
    }

    /**
     * Soft delete user
     * Mark as deleted but keep data in database
     */
    public void softDeleteUser(Long id) throws Exception {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new Exception("User with ID " + id + " not found!");
        }

        User user = userOptional.get();
        user.setIsDeleted(true);
        user.setIsActive(false);
        userRepository.save(user);
    }

    /**
     * Activate or deactivate user
     */
    public User toggleUserActive(Long id, Boolean isActive) throws Exception {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new Exception("User with ID " + id + " not found!");
        }

        User user = userOptional.get();
        user.setIsActive(isActive);
        return userRepository.save(user);
    }

    /**
     * Convert User entity to UserDTO
     * Remove password (security!)
     */
    public UserDTO convertToDTO(User user) {
        RoleDTO roleDTO = RoleDTO.builder()
                .id(user.getRole().getId())
                .name(user.getRole().getName())
                .description(user.getRole().getDescription())
                .roleType(user.getRole().getRoleType().toString())
                .build();

        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(roleDTO)
                .isActive(user.getIsActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * Convert list of users to DTOs
     */
    public List<UserDTO> convertToDTOs(List<User> users) {
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
}