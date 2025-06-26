package uz.oromland.config;

import uz.oromland.enums.Permission;
import uz.oromland.enums.Role;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Configuration class that defines the mapping between roles and their permissions
 * This centralizes the role-permission logic based on business requirements
 */
public class RolePermissionConfig {

    private static final Map<Role, Set<Permission>> ROLE_PERMISSIONS;

    static {
        ROLE_PERMISSIONS = new EnumMap<>(Role.class);
        initializeRolePermissions();
    }

    private static void initializeRolePermissions() {
        // SUPER_ADMIN - Can do everything (all permissions)
        ROLE_PERMISSIONS.put(Role.SUPER_ADMIN, EnumSet.allOf(Permission.class));

        // ADMIN - Cannot CRUD SUPER_ADMIN, cannot update Admins and SUPER_ADMIN roles,
        // but can assign roles to users and managers, can CRUD other things
        Set<Permission> adminPermissions = EnumSet.allOf(Permission.class);
        adminPermissions.removeAll(Arrays.asList(
            // Cannot manage SUPER_ADMIN
            Permission.CREATE_SUPER_ADMIN,
            Permission.UPDATE_SUPER_ADMIN,
            Permission.DELETE_SUPER_ADMIN,
            // Cannot update other ADMINs (but can read)
            Permission.UPDATE_ADMIN
        ));
        ROLE_PERMISSIONS.put(Role.ADMIN, adminPermissions);

        // MANAGER - Can read and update camps/sanatoriums they own,
        // can only read documents from their own camps/sanatoriums
        ROLE_PERMISSIONS.put(Role.MANAGER, EnumSet.of(
            // Own camp/sanatorium management
            Permission.READ_OWN_CAMP,
            Permission.UPDATE_OWN_CAMP,
            Permission.READ_OWN_SANATORIUM,
            Permission.UPDATE_OWN_SANATORIUM,
            
            // Own documents only
            Permission.READ_OWN_DOCUMENTS,
            
            // Can view their own role info
            Permission.READ_ROLE,
            
            // Can create feedback
            Permission.CREATE_FEEDBACK,
            Permission.UPDATE_FEEDBACK
        ));

        // OPERATOR - Can only read list of users, documents, and manager feedback
        ROLE_PERMISSIONS.put(Role.OPERATOR, EnumSet.of(
            Permission.READ_USER_LIST,
            Permission.READ_ALL_DOCUMENTS,
            Permission.READ_MANAGER_FEEDBACK,
            Permission.READ_ROLE
        ));

        // USER - Default role, can CRU bookings
        ROLE_PERMISSIONS.put(Role.USER, EnumSet.of(
            Permission.CREATE_BOOKING,
            Permission.READ_BOOKING,
            Permission.UPDATE_BOOKING,
            Permission.READ_ROLE,
            Permission.CREATE_FEEDBACK
        ));
    }

    /**
     * Get all permissions for a specific role
     */
    public static Set<Permission> getPermissions(Role role) {
        return new HashSet<>(ROLE_PERMISSIONS.getOrDefault(role, Collections.emptySet()));
    }

    /**
     * Check if a role has a specific permission
     */
    public static boolean hasPermission(Role role, Permission permission) {
        return ROLE_PERMISSIONS.getOrDefault(role, Collections.emptySet()).contains(permission);
    }

    /**
     * Get all roles that have a specific permission
     */
    public static Set<Role> getRolesWithPermission(Permission permission) {
        return ROLE_PERMISSIONS.entrySet().stream()
                .filter(entry -> entry.getValue().contains(permission))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    /**
     * Get permissions grouped by category for a specific role
     */
    public static Map<Permission.PermissionCategory, Set<Permission>> getPermissionsByCategory(Role role) {
        return getPermissions(role).stream()
                .collect(Collectors.groupingBy(
                    Permission::getCategory,
                    () -> new EnumMap<>(Permission.PermissionCategory.class),
                    Collectors.toSet()
                ));
    }

    /**
     * Check if a role can perform an action on another role
     * Used for role assignment/management operations
     */
    public static boolean canManageRole(Role managerRole, Role targetRole) {
        return managerRole.canManageRole(targetRole) && 
               hasPermission(managerRole, Permission.ASSIGN_ROLE);
    }

    /**
     * Get all permissions as a readable list for a role
     */
    public static List<String> getPermissionDescriptions(Role role) {
        return getPermissions(role).stream()
                .map(Permission::getDescription)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Validate if a set of permissions is valid for a role
     * This can be used when creating custom role assignments
     */
    public static boolean isValidPermissionSet(Role role, Set<Permission> permissions) {
        Set<Permission> allowedPermissions = getPermissions(role);
        return allowedPermissions.containsAll(permissions);
    }

    /**
     * Get the intersection of permissions between two roles
     * Useful for finding common permissions
     */
    public static Set<Permission> getCommonPermissions(Role role1, Role role2) {
        Set<Permission> permissions1 = getPermissions(role1);
        Set<Permission> permissions2 = getPermissions(role2);
        permissions1.retainAll(permissions2);
        return permissions1;
    }
}