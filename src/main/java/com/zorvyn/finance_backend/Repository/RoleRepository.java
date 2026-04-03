package com.zorvyn.finance_backend.Repository;


import com.zorvyn.finance_backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * RoleRepository - Database queries for roles
 *
 * Extends JpaRepository<Role, Long>:
 * - Role: Entity we're working with
 * - Long: Type of ID (primary key)
 *
 * Spring Data JPA auto-gives us:
 * save(), findById(), findAll(), delete() - NO SQL NEEDED!
 *
 * Our custom methods:
 * - findByName(String name)
 * - findByRoleType(RoleType)
 *
 * Spring reads method name and creates SQL!
 * "findBy" + "Name" = WHERE name = ?
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Find role by name
     * Usage: roleRepository.findByName("VIEWER");
     * Returns: Optional (might be empty)
     */
    Optional<Role> findByName(String name);

    /**
     * Find role by type
     * Usage: roleRepository.findByRoleType(RoleType.ADMIN);
     */
    Optional<Role> findByRoleType(Role.RoleType roleType);
}