package com.zorvyn.finance_backend.Repository;


import com.zorvyn.finance_backend.entity.User;
import com.zorvyn.finance_backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * UserRepository - Database queries for users
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email
     */
    Optional<User> findByEmail(String email);

    /**
     * Find user by email AND not deleted
     * isDeletedFalse = WHERE is_deleted = false
     */
    Optional<User> findByEmailAndIsDeletedFalse(String email);

    /**
     * Get all active (not deleted) users
     */
    List<User> findByIsDeletedFalse();

    /**
     * Get all users with specific role (not deleted)
     */
    List<User> findByRoleAndIsDeletedFalse(Role role);

    /**
     * Get all users that are active AND not deleted
     */
    List<User> findByIsDeletedFalseAndIsActiveTrue();
}
