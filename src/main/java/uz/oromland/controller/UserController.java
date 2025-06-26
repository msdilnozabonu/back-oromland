package uz.oromland.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.oromland.payload.UserDTO;
import uz.oromland.service.UserService;

import java.util.List;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {

       List<UserDTO> userDTO =  userService.getAllUsers();

       return ResponseEntity.status(201).body(userDTO);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

        UserDTO userDTO = userService.getUserById(id);

        return ResponseEntity.status(200).body(userDTO);

    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDTO userDTO) {

        userService.createUser(userDTO);

        return ResponseEntity.status(201).body("User created successfully: ");

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {

        UserDTO updatedUser = userService.updateUser(id, userDTO);

        return ResponseEntity.status(200).body(updatedUser);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.status(200).body("User deleted successfully");

    }



}
