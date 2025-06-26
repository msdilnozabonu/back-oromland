package uz.oromland.mapper;

import uz.oromland.enums.Permission;

import java.util.EnumSet;
import java.util.Set;

public class RolePermissionMapping {

    public static Set<Permission> getPermissionsForRole(String roleName) {
        return switch (roleName) {
            case "SUPER_ADMIN" -> EnumSet.allOf(Permission.class);
            case "ADMIN" ->
                // ADMIN can CRUD everything except SUPER_ADMIN and cannot CRUD ADMIN roles.
                    EnumSet.of(
                            Permission.ASSIGN_ROLE, Permission.REMOVE_ROLE, Permission.READ_ROLE,

                            Permission.CREATE_MANAGER, Permission.UPDATE_MANAGER, Permission.DELETE_MANAGER, Permission.READ_MANAGER,
                            Permission.CREATE_OPERATOR, Permission.UPDATE_OPERATOR, Permission.DELETE_OPERATOR, Permission.READ_OPERATOR,
                            Permission.CREATE_USER, Permission.UPDATE_USER, Permission.DELETE_USER, Permission.READ_USER,

                            Permission.CREATE_CAMP, Permission.UPDATE_CAMP, Permission.DELETE_CAMP, Permission.READ_CAMP,
                            Permission.CREATE_SANATORIUM, Permission.UPDATE_SANATORIUM, Permission.DELETE_SANATORIUM, Permission.READ_SANATORIUM,

                            Permission.CREATE_BOOKING, Permission.UPDATE_BOOKING, Permission.DELETE_BOOKING, Permission.READ_BOOKING,

                            Permission.CREATE_DOCUMENT, Permission.UPDATE_DOCUMENT, Permission.DELETE_DOCUMENT, Permission.READ_DOCUMENT
                    );
            case "MANAGER" ->
                // Only update connected camps/sanatoriums, accept/reject docs
                    EnumSet.of(
                            Permission.UPDATE_CAMP, Permission.READ_CAMP,
                            Permission.UPDATE_SANATORIUM, Permission.READ_SANATORIUM,
                            Permission.UPDATE_DOCUMENT, Permission.READ_DOCUMENT
                    );
            case "OPERATOR" ->
                // Can see users, see manager feedback (assuming READ_USER, READ_DOCUMENT)
                    EnumSet.of(
                            Permission.READ_USER, Permission.READ_DOCUMENT
                    );
            default -> EnumSet.noneOf(Permission.class);
        };
    }
}
