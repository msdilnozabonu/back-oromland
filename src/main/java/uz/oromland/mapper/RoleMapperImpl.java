package uz.oromland.mapper;

import org.springframework.stereotype.Service;
import uz.oromland.entity.Role;
import uz.oromland.payload.RoleDTO;

@Service
public class RoleMapperImpl implements RoleMapper{
    @Override
    public RoleDTO toDTO(Role role) {
        return new RoleDTO(
                role.getId(),
                role.getName(),
                role.getPermission()
        );
    }

    @Override
    public Role toEntity(RoleDTO roleDTO) {

        return new Role(
                roleDTO.getId(),
                roleDTO.getName(),
                roleDTO.getPermission()
        );

    }
}
