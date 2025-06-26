package uz.oromland.service;

import org.springframework.stereotype.Service;
import uz.oromland.config.RolePermissionConfig;
import uz.oromland.enums.Permission;
import uz.oromland.enums.Role;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Service class for managing role-permission operations
 * Provides business logic for role and permission management
 */
@Service
public class RolePermissionService {

    /**
     * Check if a user with a specific role has permission to perform an action
     */
    public boolean hasPermission(Role userRole, Permission requiredPermission) {
        if (userRole == null || requiredPermission == null) {
            return false;
        }
        return RolePermissionConfig.hasPermission(userRole, requiredPermission);
    }

    /**
     * Check if a user can manage (assign/remove roles) another user
     */
    public boolean canManageUser(Role managerRole, Role targetUserRole) {
        if (managerRole == null || targetUserRole == null) {
            return false;
        }
        return RolePermissionConfig.canManageRole(managerRole, targetUserRole);
    }

    /**
     * Get all permissions for a role
     */
    public Set<Permission> getUserPermissions(Role role) {
        if (role == null) {
            return Set.of();
        }
        return RolePermissionConfig.getPermissions(role);
    }

    /**
     * Get permissions grouped by category for better UI display
     */
    public Map<Permission.PermissionCategory, Set<Permission>> getPermissionsByCategory(Role role) {
        if (role == null) {
            return Map.of();
        }
        return RolePermissionConfig.getPermissionsByCategory(role);
    }

    /**
     * Get all available roles that a user can assign to others
     */
    public Set<Role> getAssignableRoles(Role managerRole) {
        if (managerRole == null) {
            return Set.of();
        }

        return Set.of(Role.values()).stream()
                .filter(role -> RolePermissionConfig.canManageRole(managerRole, role))
                .collect(java.util.stream.Collectors.toSet());
    }

    /**
     * Validate if a role assignment is allowed
     */
    public boolean isRoleAssignmentValid(Role assignerRole, Role targetRole) {
        return canManageUser(assignerRole, targetRole);
    }

    /**
     * Get human-readable permission descriptions for a role
     */
    public List<String> getPermissionDescriptions(Role role) {
        if (role == null) {
            return List.of();
        }
        return RolePermissionConfig.getPermissionDescriptions(role);
    }

    /**
     * Check if a user has any of the required permissions (OR operation)
     */
    public boolean hasAnyPermission(Role userRole, Permission... requiredPermissions) {
        if (userRole == null || requiredPermissions == null || requiredPermissions.length == 0) {
            return false;
        }

        for (Permission permission : requiredPermissions) {
            if (hasPermission(userRole, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a user has all of the required permissions (AND operation)
     */
    public boolean hasAllPermissions(Role userRole, Permission... requiredPermissions) {
        if (userRole == null || requiredPermissions == null || requiredPermissions.length == 0) {
            return false;
        }

        for (Permission permission : requiredPermissions) {
            if (!hasPermission(userRole, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get roles that have a specific permission
     */
    public Set<Role> getRolesWithPermission(Permission permission) {
        if (permission == null) {
            return Set.of();
        }
        return RolePermissionConfig.getRolesWithPermission(permission);
    }

    /**
     * Check if a role is higher in hierarchy than another
     */
    public boolean hasHigherAuthority(Role role1, Role role2) {
        if (role1 == null || role2 == null) {
            return false;
        }
        return role1.hasHigherAuthorityThan(role2);
    }

    /**
     * Get the highest role from a collection of roles
     */
    public Role getHighestRole(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return null;
        }

        return roles.stream()
                .min((r1, r2) -> Integer.compare(r1.getHierarchyLevel(), r2.getHierarchyLevel()))
                .orElse(null);
    }

    /**
     * Check if a user can access a specific resource based on ownership
     * This is used for managers who can only access their own camps/sanatoriums
     */
    public boolean canAccessOwnedResource(Role userRole, Long userId, Long resourceOwnerId) {
        if (userRole == null || userId == null || resourceOwnerId == null) {
            return false;
        }

        // SUPER_ADMIN and ADMIN can access all resources
        if (userRole == Role.SUPER_ADMIN || userRole == Role.ADMIN) {
            return true;
        }

        // For other roles, they can only access resources they own
        return userId.equals(resourceOwnerId);
    }

    /**
     * Get default role for new users
     */
    public Role getDefaultRole() {
        return Role.USER;
    }

    /**
     * Check if a role can be deleted/deactivated
     * Some roles might be protected from deletion
     */
    public boolean canDeleteRole(Role deleterRole, Role targetRole) {
        // SUPER_ADMIN role cannot be deleted
        if (targetRole == Role.SUPER_ADMIN) {
            return false;
        }

        // Only higher authority roles can delete lower roles
        return hasHigherAuthority(deleterRole, targetRole) && 
               hasPermission(deleterRole, Permission.ASSIGN_ROLE);
    }
}