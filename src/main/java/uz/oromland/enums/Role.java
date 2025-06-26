package uz.oromland.enums;

/**
 * Role enum defines the hierarchy of user roles in the system
 * Each role has different levels of access and permissions
 */
public enum Role {
    /**
     * Highest level - can do everything, has all permissions
     */
    SUPER_ADMIN("Super Administrator", 1),
    
    /**
     * High level admin - cannot CRUD SUPER_ADMIN, cannot update Admins and SUPER_ADMIN roles,
     * but can assign roles to users and managers, can CRUD other entities
     */
    ADMIN("Administrator", 2),
    
    /**
     * Mid-level role - can read and update camps/sanatoriums they own,
     * can only read documents from their own camps/sanatoriums
     */
    MANAGER("Manager", 3),
    
    /**
     * Low-level role - can only read list of users, documents, and manager feedback
     */
    OPERATOR("Operator", 4),
    
    /**
     * Default role - can Create, Read, Update bookings
     */
    USER("User", 5);

    private final String displayName;
    private final int hierarchyLevel;

    Role(String displayName, int hierarchyLevel) {
        this.displayName = displayName;
        this.hierarchyLevel = hierarchyLevel;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getHierarchyLevel() {
        return hierarchyLevel;
    }

    /**
     * Check if this role has higher authority than another role
     * Lower hierarchy level means higher authority
     */
    public boolean hasHigherAuthorityThan(Role other) {
        return this.hierarchyLevel < other.hierarchyLevel;
    }

    /**
     * Check if this role can manage (assign/remove roles) another role
     */
    public boolean canManageRole(Role targetRole) {
        switch (this) {
            case SUPER_ADMIN:
                return true; // Can manage all roles
            case ADMIN:
                // Cannot manage SUPER_ADMIN or other ADMINs
                return targetRole != SUPER_ADMIN && targetRole != ADMIN;
            default:
                return false; // Other roles cannot manage roles
        }
    }
}