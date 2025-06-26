package uz.oromland.service;

import uz.oromland.payload.RoleDTO;
import uz.oromland.entity.User;
import java.util.List;

public interface RoleService {
    RoleDTO findById(Integer roleId, User user);
    RoleDTO findByIdInternal(Integer roleId); // Internal method for mappers
    List<RoleDTO> findByAll(User user);
    RoleDTO createRole(RoleDTO roleDTO, User user);
    RoleDTO updateRole(Integer id, RoleDTO roleDTO, User user);
    void deleteRole(Integer id, User user);
}
