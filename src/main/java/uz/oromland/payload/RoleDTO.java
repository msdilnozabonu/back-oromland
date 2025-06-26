package uz.oromland.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.oromland.entity.Role;
import uz.oromland.enums.Permission;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link Role}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO implements Serializable {
    private Integer id;
    private String name;
    private List<Permission> permission;
}