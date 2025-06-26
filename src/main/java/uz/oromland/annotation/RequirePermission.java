package uz.oromland.annotation;

import uz.oromland.enums.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods that require specific permissions
 * Can be used on controller methods or service methods for authorization
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    
    /**
     * Required permissions (OR operation - user needs any one of these)
     */
    Permission[] value() default {};
    
    /**
     * All required permissions (AND operation - user needs all of these)
     */
    Permission[] allOf() default {};
    
    /**
     * Description of what this permission check is for
     */
    String description() default "";
    
    /**
     * Whether to check ownership for resource-based permissions
     * Used for managers who can only access their own resources
     */
    boolean checkOwnership() default false;
}