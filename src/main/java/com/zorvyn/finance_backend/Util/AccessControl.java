package com.zorvyn.finance_backend.Util;

import com.zorvyn.finance_backend.entity.User;
import com.zorvyn.finance_backend.entity.Role;

/**
 * AccessControl - Check permissions
 *
 * What each role can do:
 * VIEWER: Can only view
 * ANALYST: Can create records
 * ADMIN: Can do everything
 */
public class AccessControl {

    /**
     * Can user create records?
     */
    public static boolean canCreateRecord(User user) {
        // If inactive, no access
        if (!user.getIsActive()) {
            return false;
        }

        Role.RoleType roleType = user.getRole().getRoleType();

        // VIEWER cannot create
        if (roleType == Role.RoleType.VIEWER) {
            return false;
        }

        // ANALYST and ADMIN can create
        return roleType == Role.RoleType.ANALYST || roleType == Role.RoleType.ADMIN;
    }

    /**
     * Can user view records?
     */
    public static boolean canViewRecords(User user) {
        return user.getIsActive();
    }

    /**
     * Can user update records?
     */
    public static boolean canUpdateRecord(User user) {
        if (!user.getIsActive()) {
            return false;
        }

        Role.RoleType roleType = user.getRole().getRoleType();

        if (roleType == Role.RoleType.VIEWER) {
            return false;
        }

        return roleType == Role.RoleType.ANALYST || roleType == Role.RoleType.ADMIN;
    }

    /**
     * Can user delete records?
     */
    public static boolean canDeleteRecord(User user) {
        if (!user.getIsActive()) {
            return false;
        }

        // Only ADMIN can delete
        return user.getRole().getRoleType() == Role.RoleType.ADMIN;
    }
}
