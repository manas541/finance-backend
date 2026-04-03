package com.zorvyn.finance_backend.Controller;

import com.zorvyn.finance_backend.entity.Role;
import com.zorvyn.finance_backend.Service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * RoleController - REST API endpoints for roles
 *
 * @RestController: This is an API endpoint class
 * @RequestMapping("/roles"): All methods start with /api/v1/roles
 *
 * HTTP Methods:
 * GET: Retrieve data
 * POST: Create data
 * PUT: Update data
 * DELETE: Delete data
 *
 * ResponseEntity: Control response (status code + data)
 */
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    /**
     * Service auto-injected
     */
    private final RoleService roleService;

    /**
     * GET /api/v1/roles
     * Get all roles
     *
     * ResponseEntity.ok(): HTTP 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * GET /api/v1/roles/{id}
     * Get role by ID
     *
     * @PathVariable Long id: Get ID from URL
     * Example: GET /api/v1/roles/1 → id = 1
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable Long id) {
        Optional<Role> role = roleService.getRoleById(id);

        // If not found, return 404
        if (role.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Role with ID " + id + " not found");
        }

        // Return 200 OK with role
        return ResponseEntity.ok(role.get());
    }

    /**
     * POST /api/v1/roles
     * Create new role
     *
     * @RequestBody Role role: Get JSON from request body
     * Example:
     * {
     *     "name": "VIEWER",
     *     "description": "Can view dashboard",
     *     "roleType": "VIEWER"
     * }
     *
     * ResponseEntity.status(HttpStatus.CREATED): HTTP 201
     */
    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        try {
            Role createdRole = roleService.createRole(role);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
        } catch (Exception e) {
            // HTTP 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * PUT /api/v1/roles/{id}
     * Update role
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateRole(
            @PathVariable Long id,
            @RequestBody Role role) {
        try {
            Role updatedRole = roleService.updateRole(id, role);
            return ResponseEntity.ok(updatedRole);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * DELETE /api/v1/roles/{id}
     * Delete role
     *
     * ResponseEntity.noContent(): HTTP 204 No Content (empty body)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: " + e.getMessage());
        }
    }
}