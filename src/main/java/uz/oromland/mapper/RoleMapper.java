package uz.oromland.mapper;

import uz.oromland.entity.Role;
import uz.oromland.payload.RoleDTO;

public interface RoleMapper {
    RoleDTO toDTO(Role role);

    Role toEntity(RoleDTO roleDTO);

}
