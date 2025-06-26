package uz.oromland.enums;

import lombok.Getter;

/**
 * Permission enum defines all possible actions/permissions in the system
 * These permissions are assigned to roles based on business requirements
 */
@Getter
public enum Permission {

    // Role Management Permissions
    ASSIGN_ROLE("Assign roles to users"),
    REMOVE_ROLE("Remove roles from users"),
    READ_ROLE("View role information"),

    // User Management Permissions
    CREATE_SUPER_ADMIN("Create Super Admin users"),
    UPDATE_SUPER_ADMIN("Update Super Admin users"),
    DELETE_SUPER_ADMIN("Delete Super Admin users"),
    READ_SUPER_ADMIN("View Super Admin users"),

    CREATE_ADMIN("Create Admin users"),
    UPDATE_ADMIN("Update Admin users"),
    DELETE_ADMIN("Delete Admin users"),
    READ_ADMIN("View Admin users"),

    CREATE_MANAGER("Create Manager users"),
    UPDATE_MANAGER("Update Manager users"),
    DELETE_MANAGER("Delete Manager users"),
    READ_MANAGER("View Manager users"),

    CREATE_OPERATOR("Create Operator users"),
    UPDATE_OPERATOR("Update Operator users"),
    DELETE_OPERATOR("Delete Operator users"),
    READ_OPERATOR("View Operator users"),

    CREATE_USER("Create regular users"),
    UPDATE_USER("Update regular users"),
    DELETE_USER("Delete regular users"),
    READ_USER("View regular users"),
    READ_USER_LIST("View list of all users"),

    // Camp Management Permissions
    CREATE_CAMP("Create camps"),
    UPDATE_CAMP("Update camps"),
    DELETE_CAMP("Delete camps"),
    READ_CAMP("View camps"),
    UPDATE_OWN_CAMP("Update own managed camps"),
    READ_OWN_CAMP("View own managed camps"),

    // Sanatorium Management Permissions
    CREATE_SANATORIUM("Create sanatoriums"),
    UPDATE_SANATORIUM("Update sanatoriums"),
    DELETE_SANATORIUM("Delete sanatoriums"),
    READ_SANATORIUM("View sanatoriums"),
    UPDATE_OWN_SANATORIUM("Update own managed sanatoriums"),
    READ_OWN_SANATORIUM("View own managed sanatoriums"),

    // Booking Management Permissions
    CREATE_BOOKING("Create bookings"),
    UPDATE_BOOKING("Update bookings"),
    DELETE_BOOKING("Delete bookings"),
    READ_BOOKING("View bookings"),
    READ_ALL_BOOKINGS("View all bookings"),

    // Document Management Permissions
    CREATE_DOCUMENT("Create documents"),
    UPDATE_DOCUMENT("Update documents"),
    DELETE_DOCUMENT("Delete documents"),
    READ_DOCUMENT("View documents"),
    READ_OWN_DOCUMENTS("View own camp/sanatorium documents"),
    READ_ALL_DOCUMENTS("View all documents"),

    // Feedback Management Permissions
    READ_MANAGER_FEEDBACK("View manager feedback"),
    CREATE_FEEDBACK("Create feedback"),
    UPDATE_FEEDBACK("Update feedback"),
    DELETE_FEEDBACK("Delete feedback");

    private final String description;

    Permission(String description) {
        this.description = description;
    }

    /**
     * Get the category of this permission for grouping
     */
    public PermissionCategory getCategory() {
        String name = this.name();
        if (name.contains("ROLE")) {
            return PermissionCategory.ROLE_MANAGEMENT;
        } else if (name.contains("USER") || name.contains("ADMIN") || name.contains("MANAGER") || 
                   name.contains("OPERATOR") || name.contains("SUPER_ADMIN")) {
            return PermissionCategory.USER_MANAGEMENT;
        } else if (name.contains("CAMP")) {
            return PermissionCategory.CAMP_MANAGEMENT;
        } else if (name.contains("SANATORIUM")) {
            return PermissionCategory.SANATORIUM_MANAGEMENT;
        } else if (name.contains("BOOKING")) {
            return PermissionCategory.BOOKING_MANAGEMENT;
        } else if (name.contains("DOCUMENT")) {
            return PermissionCategory.DOCUMENT_MANAGEMENT;
        } else if (name.contains("FEEDBACK")) {
            return PermissionCategory.FEEDBACK_MANAGEMENT;
        }
        return PermissionCategory.OTHER;
    }

    public enum PermissionCategory {
        ROLE_MANAGEMENT("Role Management"),
        USER_MANAGEMENT("User Management"),
        CAMP_MANAGEMENT("Camp Management"),
        SANATORIUM_MANAGEMENT("Sanatorium Management"),
        BOOKING_MANAGEMENT("Booking Management"),
        DOCUMENT_MANAGEMENT("Document Management"),
        FEEDBACK_MANAGEMENT("Feedback Management"),
        OTHER("Other");

        private final String displayName;

        PermissionCategory(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
