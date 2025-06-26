package uz.oromland.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.oromland.entity.Attachment;
import uz.oromland.entity.Role;
import uz.oromland.entity.User;
import uz.oromland.payload.AttachmentDTO;
import uz.oromland.payload.RoleDTO;
import uz.oromland.payload.UserDTO;
import uz.oromland.service.AttachmentService;
import uz.oromland.service.RoleService;


@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final RoleService roleService;
    private final RoleMapper roleMapper;
    private final AttachmentService attachmentService;
    private final AttachmentMapper attachmentMapper;


    @Override
    public UserDTO toDto(User user) {

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getPassword(),
                user.getIsActive(),
                user.getBirthDate(),
                user.getGender(),
                user.getRole() == null ? null : user.getRole().getId(),
                user.getAttachment() == null ? null : user.getAttachment().getId()
        );

    }

    @Override
    public User toEntity(UserDTO userDTO) {

        RoleDTO roleDTO = userDTO.getRoleId() != null ? 
            roleService.findByIdInternal(userDTO.getRoleId()) : null;
        Role role = roleMapper.toEntity(roleDTO);

        AttachmentDTO attachmentDTO = attachmentService.getById(userDTO.getAttachmentId());
        Attachment attachment = attachmentMapper.toEntity(attachmentDTO);

        return new User(
                userDTO.getId(),
                userDTO.getUsername(),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                userDTO.getEmail(),
                userDTO.getPhoneNumber(),
                userDTO.getPassword(),
                userDTO.getIsActive(),
                userDTO.getBirthDate(),
                userDTO.getGender(),
                role,
                attachment
        );
    }
}
