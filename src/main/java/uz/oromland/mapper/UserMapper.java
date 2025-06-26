package uz.oromland.mapper;

import uz.oromland.entity.User;
import uz.oromland.payload.UserDTO;

public interface UserMapper {
    UserDTO toDto(User user);

    User toEntity(UserDTO userDTO);
}
