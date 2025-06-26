package uz.oromland.util;

import lombok.Setter;
import uz.oromland.enums.Permission;
import uz.oromland.enums.Role;
import uz.oromland.service.RolePermissionService;

/**
 * Utility class for permission-related operations
 * Provides static methods for common permission checks
 */
public class PermissionUtil {

    // Static initialization - would be better to use Spring's @Autowired in a real application
    @Setter
    private static RolePermissionService rolePermissionService;

    /**
     * Quick permission check
     */
    public static boolean hasPermission(Role userRole, Permission permission) {
        if (rolePermissionService != null) {
            return rolePermissionService.hasPermission(userRole, permission);
        }
        // Fallback to direct config check
        return uz.oromland.config.RolePermissionConfig.hasPermission(userRole, permission);
    }

    /**
     * Check if user can manage another user's role
     */
    public static boolean canManageRole(Role managerRole, Role targetRole) {
        if (rolePermissionService != null) {
            return rolePermissionService.canManageUser(managerRole, targetRole);
        }
        // Fallback to direct config check
        return uz.oromland.config.RolePermissionConfig.canManageRole(managerRole, targetRole);
    }

    /**
     * Common permission checks for different operations
     */
    public static class UserOperations {
        public static boolean canCreateUser(Role role) {
            return hasPermission(role, Permission.CREATE_USER);
        }

        public static boolean canUpdateUser(Role role) {
            return hasPermission(role, Permission.UPDATE_USER);
        }

        public static boolean canDeleteUser(Role role) {
            return hasPermission(role, Permission.DELETE_USER);
        }

        public static boolean canViewUsers(Role role) {
            return hasPermission(role, Permission.READ_USER) || 
                   hasPermission(role, Permission.READ_USER_LIST);
        }
    }

    public static class CampOperations {
        public static boolean canCreateCamp(Role role) {
            return hasPermission(role, Permission.CREATE_CAMP);
        }

        public static boolean canUpdateCamp(Role role) {
            return hasPermission(role, Permission.UPDATE_CAMP) || 
                   hasPermission(role, Permission.UPDATE_OWN_CAMP);
        }

        public static boolean canDeleteCamp(Role role) {
            return hasPermission(role, Permission.DELETE_CAMP);
        }

        public static boolean canViewCamps(Role role) {
            return hasPermission(role, Permission.READ_CAMP) || 
                   hasPermission(role, Permission.READ_OWN_CAMP);
        }

        public static boolean canUpdateOwnCamp(Role role) {
            return hasPermission(role, Permission.UPDATE_OWN_CAMP);
        }
    }

    public static class SanatoriumOperations {
        public static boolean canCreateSanatorium(Role role) {
            return hasPermission(role, Permission.CREATE_SANATORIUM);
        }

        public static boolean canUpdateSanatorium(Role role) {
            return hasPermission(role, Permission.UPDATE_SANATORIUM) || 
                   hasPermission(role, Permission.UPDATE_OWN_SANATORIUM);
        }

        public static boolean canDeleteSanatorium(Role role) {
            return hasPermission(role, Permission.DELETE_SANATORIUM);
        }

        public static boolean canViewSanatoriums(Role role) {
            return hasPermission(role, Permission.READ_SANATORIUM) || 
                   hasPermission(role, Permission.READ_OWN_SANATORIUM);
        }

        public static boolean canUpdateOwnSanatorium(Role role) {
            return hasPermission(role, Permission.UPDATE_OWN_SANATORIUM);
        }
    }

    public static class BookingOperations {
        public static boolean canCreateBooking(Role role) {
            return hasPermission(role, Permission.CREATE_BOOKING);
        }

        public static boolean canUpdateBooking(Role role) {
            return hasPermission(role, Permission.UPDATE_BOOKING);
        }

        public static boolean canDeleteBooking(Role role) {
            return hasPermission(role, Permission.DELETE_BOOKING);
        }

        public static boolean canViewBookings(Role role) {
            return hasPermission(role, Permission.READ_BOOKING) || 
                   hasPermission(role, Permission.READ_ALL_BOOKINGS);
        }
    }

    public static class DocumentOperations {
        public static boolean canCreateDocument(Role role) {
            return hasPermission(role, Permission.CREATE_DOCUMENT);
        }

        public static boolean canUpdateDocument(Role role) {
            return hasPermission(role, Permission.UPDATE_DOCUMENT);
        }

        public static boolean canDeleteDocument(Role role) {
            return hasPermission(role, Permission.DELETE_DOCUMENT);
        }

        public static boolean canViewDocuments(Role role) {
            return hasPermission(role, Permission.READ_DOCUMENT) || 
                   hasPermission(role, Permission.READ_OWN_DOCUMENTS) ||
                   hasPermission(role, Permission.READ_ALL_DOCUMENTS);
        }

        public static boolean canViewOwnDocuments(Role role) {
            return hasPermission(role, Permission.READ_OWN_DOCUMENTS);
        }

        public static boolean canViewAllDocuments(Role role) {
            return hasPermission(role, Permission.READ_ALL_DOCUMENTS);
        }
    }

    public static class RoleOperations {
        public static boolean canAssignRole(Role role) {
            return hasPermission(role, Permission.ASSIGN_ROLE);
        }

        public static boolean canRemoveRole(Role role) {
            return hasPermission(role, Permission.REMOVE_ROLE);
        }

        public static boolean canViewRoles(Role role) {
            return hasPermission(role, Permission.READ_ROLE);
        }
    }

    /**
     * Helper method to get role display name
     */
    public static String getRoleDisplayName(Role role) {
        return role != null ? role.getDisplayName() : "Unknown";
    }

    /**
     * Helper method to get permission description
     */
    public static String getPermissionDescription(Permission permission) {
        return permission != null ? permission.getDescription() : "Unknown";
    }
}