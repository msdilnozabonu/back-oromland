package uz.oromland.service;

import uz.oromland.payload.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    void createUser(UserDTO userDTO);

    UserDTO updateUser(Long id, UserDTO userDTO);

    void deleteUser(Long id);
}
