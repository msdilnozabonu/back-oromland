package uz.oromland.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.oromland.annotation.RequirePermission;
import uz.oromland.enums.Permission;
import uz.oromland.enums.Role;
import uz.oromland.service.RolePermissionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Controller for managing roles and permissions
 * Demonstrates how to use the permission system
 */
@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    /**
     * Get all available roles
     * Only accessible by users who can read roles
     */
    @GetMapping
    @RequirePermission(Permission.READ_ROLE)
    public ResponseEntity<Role[]> getAllRoles() {
        Role[] values = Role.values();
        return ResponseEntity.ok(values);
    }

    /**
     * Get permissions for a specific role
     */
    @GetMapping("/{role}/permissions")
    @RequirePermission(Permission.READ_ROLE)
    public ResponseEntity<Map<String, Object>> getRolePermissions(@PathVariable Role role) {
        Set<Permission> permissions = rolePermissionService.getUserPermissions(role);
        Map<Permission.PermissionCategory, Set<Permission>> permissionsByCategory = 
            rolePermissionService.getPermissionsByCategory(role);
        List<String> descriptions = rolePermissionService.getPermissionDescriptions(role);

        Map<String, Object> response = new HashMap<>();
        response.put("role", role);
        response.put("roleName", role.getDisplayName());
        response.put("permissions", permissions);
        response.put("permissionsByCategory", permissionsByCategory);
        response.put("descriptions", descriptions);
        response.put("permissionCount", permissions.size());

        return ResponseEntity.ok(response);
    }

    /**
     * Check if a role has a specific permission
     */
    @GetMapping("/{role}/has-permission/{permission}")
    @RequirePermission(Permission.READ_ROLE)
    public ResponseEntity<Map<String, Object>> checkRolePermission(
            @PathVariable Role role, 
            @PathVariable Permission permission) {
        
        boolean hasPermission = rolePermissionService.hasPermission(role, permission);
        
        Map<String, Object> response = new HashMap<>();
        response.put("role", role);
        response.put("permission", permission);
        response.put("hasPermission", hasPermission);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get roles that can be assigned by a specific role
     * Only accessible by users who can assign roles
     */
    @GetMapping("/assignable-by/{managerRole}")
    @RequirePermission(Permission.ASSIGN_ROLE)
    public ResponseEntity<Map<String, Object>> getAssignableRoles(@PathVariable Role managerRole) {
        Set<Role> assignableRoles = rolePermissionService.getAssignableRoles(managerRole);
        
        Map<String, Object> response = new HashMap<>();
        response.put("managerRole", managerRole);
        response.put("assignableRoles", assignableRoles);
        response.put("canAssignCount", assignableRoles.size());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Check if one role can manage another role
     */
    @GetMapping("/can-manage")
    @RequirePermission(Permission.READ_ROLE)
    public ResponseEntity<Map<String, Object>> checkRoleManagement(
            @RequestParam Role managerRole,
            @RequestParam Role targetRole) {
        
        boolean canManage = rolePermissionService.canManageUser(managerRole, targetRole);
        boolean hasHigherAuthority = rolePermissionService.hasHigherAuthority(managerRole, targetRole);
        
        Map<String, Object> response = new HashMap<>();
        response.put("managerRole", managerRole);
        response.put("targetRole", targetRole);
        response.put("canManage", canManage);
        response.put("hasHigherAuthority", hasHigherAuthority);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get all permissions available in the system
     */
    @GetMapping("/permissions")
    @RequirePermission(Permission.READ_ROLE)
    public ResponseEntity<Map<String, Object>> getAllPermissions() {
        Permission[] allPermissions = Permission.values();
        Map<Permission.PermissionCategory, List<Permission>> permissionsByCategory = new HashMap<>();
        
        for (Permission permission : allPermissions) {
            Permission.PermissionCategory category = permission.getCategory();
            permissionsByCategory.computeIfAbsent(category, k -> new java.util.ArrayList<>()).add(permission);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("allPermissions", allPermissions);
        response.put("permissionsByCategory", permissionsByCategory);
        response.put("totalCount", allPermissions.length);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Get roles that have a specific permission
     */
    @GetMapping("/permissions/{permission}/roles")
    @RequirePermission(Permission.READ_ROLE)
    public ResponseEntity<Map<String, Object>> getRolesWithPermission(@PathVariable Permission permission) {
        Set<Role> rolesWithPermission = rolePermissionService.getRolesWithPermission(permission);
        
        Map<String, Object> response = new HashMap<>();
        response.put("permission", permission);
        response.put("permissionDescription", permission.getDescription());
        response.put("rolesWithPermission", rolesWithPermission);
        response.put("roleCount", rolesWithPermission.size());
        
        return ResponseEntity.ok(response);
    }

    /**
     * Simulate role assignment (this would typically involve user management)
     * Only accessible by users who can assign roles
     */
    @PostMapping("/assign")
    @RequirePermission(Permission.ASSIGN_ROLE)
    public ResponseEntity<Map<String, Object>> simulateRoleAssignment(
            @RequestParam Role assignerRole,
            @RequestParam Role targetRole,
            @RequestParam Long targetUserId) {
        
        boolean isValidAssignment = rolePermissionService.isRoleAssignmentValid(assignerRole, targetRole);
        
        Map<String, Object> response = new HashMap<>();
        response.put("assignerRole", assignerRole);
        response.put("targetRole", targetRole);
        response.put("targetUserId", targetUserId);
        response.put("isValidAssignment", isValidAssignment);
        response.put("message", isValidAssignment ? 
            "Role assignment is valid" : 
            "Role assignment is not allowed");
        
        return ResponseEntity.ok(response);
    }
}