package uz.oromland.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.oromland.entity.Role;
import uz.oromland.entity.User;
import uz.oromland.enums.Permission;
import uz.oromland.exception.RoleNotFoundException;
import uz.oromland.mapper.RoleMapper;
import uz.oromland.mapper.RolePermissionMapping;
import uz.oromland.payload.RoleDTO;
import uz.oromland.repository.RoleRepository;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
//    private final RoleRepository roleRepository;
//    private final RoleMapper roleMapper;

   /* @Override
    public RoleDTO findById(Integer roleId, User user) {

        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        return roleMapper.toDTO(role);

    }

    @Override
    public List<RoleDTO> findByAll() {

        List<Role> roleList = roleRepository.findAll();

        return roleList.stream().map(roleMapper::toDTO).toList();

    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {

        Role role = new Role(
                null,
                roleDTO.getName(),
                roleDTO.getPermission()
        );

        Role savedRole = roleRepository.save(role);
        return roleMapper.toDTO(savedRole);

    }

    @Override
    public RoleDTO updateRole(Integer id, RoleDTO roleDTO) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        role.setName(roleDTO.getName());
        role.setPermission(roleDTO.getPermission());

        Role updatedRole = roleRepository.save(role);
        return roleMapper.toDTO(updatedRole);
    }

    @Override
    public void deleteRole(Integer id) {

        Role role = roleRepository.findById(id).orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
        roleRepository.delete(role);
    }
*/
   private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    private boolean hasPermission(User user, Permission permission) {
        if (user == null || user.getRole() == null) return false;
        Set<Permission> allowed = RolePermissionMapping.getPermissionsForRole(user.getRole().getName());
        return allowed.contains(permission);
    }

    @Override
    public RoleDTO findById(Integer roleId, User user) {
        // Example: only allow reading roles if user has READ_ROLE permission
        if (!hasPermission(user, Permission.READ_ROLE)) {
            throw new SecurityException("You do not have permission to read roles.");
        }
        return findByIdInternal(roleId);
    }

    @Override
    public RoleDTO findByIdInternal(Integer roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleId));
        return roleMapper.toDTO(role);
    }

    @Override
    public List<RoleDTO> findByAll(User user) {
        if (!hasPermission(user, Permission.READ_ROLE)) {
            throw new SecurityException("You do not have permission to read roles.");
        }
        List<Role> roleList = roleRepository.findAll();
        return roleList.stream().map(roleMapper::toDTO).toList();
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO, User user) {
        // Only SUPER_ADMIN and ADMIN can create roles, but ADMIN cannot create SUPER_ADMIN or ADMIN
        if (!hasPermission(user, Permission.ASSIGN_ROLE)) {
            throw new SecurityException("You do not have permission to create roles.");
        }
        String newRoleName = roleDTO.getName();
        if (isForbiddenAdminAction(user, newRoleName, "CREATE")) {
            throw new SecurityException("ADMIN cannot create SUPER_ADMIN or ADMIN roles.");
        }
        Role role = new Role(null, newRoleName, roleDTO.getPermission());
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDTO(savedRole);
    }

    @Override
    public RoleDTO updateRole(Integer id, RoleDTO roleDTO, User user) {
        if (!hasPermission(user, Permission.ASSIGN_ROLE)) {
            throw new SecurityException("You do not have permission to update roles.");
        }
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
        String updatedRoleName = roleDTO.getName();
        if (isForbiddenAdminAction(user, updatedRoleName, "UPDATE")) {
            throw new SecurityException("ADMIN cannot update SUPER_ADMIN or ADMIN roles.");
        }
        role.setName(updatedRoleName);
        role.setPermission(roleDTO.getPermission());
        Role updatedRole = roleRepository.save(role);
        return roleMapper.toDTO(updatedRole);
    }

    @Override
    public void deleteRole(Integer id, User user) {
        if (!hasPermission(user, Permission.REMOVE_ROLE)) {
            throw new SecurityException("You do not have permission to delete roles.");
        }
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + id));
        if (isForbiddenAdminAction(user, role.getName(), "DELETE")) {
            throw new SecurityException("ADMIN cannot delete SUPER_ADMIN or ADMIN roles.");
        }
        roleRepository.delete(role);
    }

    private boolean isForbiddenAdminAction(User user, String roleName, String action) {
        // ADMIN cannot create/update/delete SUPER_ADMIN or ADMIN
        String userRole = user.getRole().getName();
        if ("ADMIN".equals(userRole) &&
                ("SUPER_ADMIN".equals(roleName) || "ADMIN".equals(roleName))) {
            return true;
        }
        return false;
    }



}
