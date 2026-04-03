package com.zorvyn.finance_backend.Service;


import com.zorvyn.finance_backend.entity.Role;
import com.zorvyn.finance_backend.Repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * RoleService - Business logic for roles
 *
 * @Service: Tell Spring this is a service
 * @RequiredArgsConstructor: Auto-inject RoleRepository
 *
 * Flow:
 * Controller calls RoleService
 * RoleService calls RoleRepository
 * RoleRepository talks to database
 * Result goes back to Controller
 * Controller returns HTTP response
 */
@Service
@RequiredArgsConstructor
public class RoleService {

    /**
     * final: Cannot change after creation
     * @RequiredArgsConstructor injects this automatically
     */
    private final RoleRepository roleRepository;

    /**
     * Get all roles from database
     * Usage: List<Role> roles = roleService.getAllRoles();
     */
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    /**
     * Get role by ID
     * Usage: Optional<Role> role = roleService.getRoleById(1L);
     *
     * Optional: Might be empty (not found)
     * - if (role.isPresent()) { use role.get(); }
     */
    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    /**
     * Get role by name
     * Usage: roleService.getRoleByName("VIEWER");
     */
    public Optional<Role> getRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    /**
     * Get role by type
     * Usage: roleService.getRoleByType(RoleType.ADMIN);
     */
    public Optional<Role> getRoleByType(Role.RoleType roleType) {
        return roleRepository.findByRoleType(roleType);
    }

    /**
     * Create new role
     *
     * Business logic:
     * 1. Check if role already exists
     * 2. If yes, throw error (no duplicates!)
     * 3. If no, save to database
     * 4. Return saved role
     *
     * @throws Exception if role exists
     */
    public Role createRole(Role role) throws Exception {
        // Check if exists
        Optional<Role> existing = roleRepository.findByName(role.getName());

        // If exists, throw error
        if (existing.isPresent()) {
            throw new Exception("Role with name '" + role.getName() + "' already exists!");
        }

        // Save and return
        return roleRepository.save(role);
    }

    /**
     * Update existing role
     */
    public Role updateRole(Long id, Role updatedRole) throws Exception {
        // Find the role
        Optional<Role> existing = roleRepository.findById(id);

        // If not found, throw error
        if (existing.isEmpty()) {
            throw new Exception("Role with ID " + id + " not found!");
        }

        Role role = existing.get();

        // Update fields
        if (updatedRole.getName() != null) {
            role.setName(updatedRole.getName());
        }
        if (updatedRole.getDescription() != null) {
            role.setDescription(updatedRole.getDescription());
        }
        if (updatedRole.getRoleType() != null) {
            role.setRoleType(updatedRole.getRoleType());
        }

        // Save and return
        return roleRepository.save(role);
    }

    /**
     * Delete role
     */
    public void deleteRole(Long id) throws Exception {
        // Check exists
        if (!roleRepository.existsById(id)) {
            throw new Exception("Role with ID " + id + " not found!");
        }

        // Delete
        roleRepository.deleteById(id);
    }
}