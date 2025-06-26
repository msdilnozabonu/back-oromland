package uz.oromland.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.oromland.entity.Role;
import uz.oromland.exception.RoleNotFoundException;

import uz.oromland.exception.UserNotFoundException;
import uz.oromland.mapper.UserMapper;
import uz.oromland.entity.User;
import uz.oromland.payload.UserDTO;
import uz.oromland.repository.RoleRepository;
import uz.oromland.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public List<UserDTO> getAllUsers() {

        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toDto).toList();

    }

    @Override
    public UserDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return userMapper.toDto(user);

    }

    @Override
    public void createUser(UserDTO userDTO) {

       Role role = roleRepository.findById(userDTO.getRoleId()).orElseThrow(()-> new RoleNotFoundException("Role not found with id: " + userDTO.getRoleId()));

        new User(
                null,
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
                null
        );

    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + userDTO.getRoleId()));

        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setPassword(userDTO.getPassword());
        user.setIsActive(userDTO.getIsActive());
        user.setBirthDate(userDTO.getBirthDate());
        user.setGender(userDTO.getGender());
        user.setRole(role);

        userRepository.save(user);

        return userMapper.toDto(user);


    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userRepository.delete(user);


    }
}

