package uz.oromland.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.oromland.config.JwtService;
import uz.oromland.entity.Role;
import uz.oromland.entity.User;
import uz.oromland.payload.AuthDTO;
import uz.oromland.payload.RegisterDTO;
import uz.oromland.repository.RoleRepository;
import uz.oromland.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<?> login(AuthDTO authDTO) {
        var authToken = new UsernamePasswordAuthenticationToken(
                authDTO.getUsername(), authDTO.getPassword()
        );
        authManager.authenticate(authToken);

        String token = jwtService.generateToken(authDTO.getUsername());

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
    
    @Override
    public ResponseEntity<?> register(RegisterDTO registerDTO) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(registerDTO.getUsername())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("message", "Username already exists"));
        }
        
        if (userRepository.existsByEmail(registerDTO.getEmail())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("message", "Email already exists"));
        }
        
        // Get role with ID 5 (default role for new users)
        Role defaultRole = roleRepository.findById(5)
                .orElseThrow(() -> new IllegalStateException("Default role with ID 5 not found"));
        
        // Create new user
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setPhoneNumber(registerDTO.getPhoneNumber());
        user.setBirthDate(registerDTO.getBirthDate());
        user.setGender(registerDTO.getGender());
        user.setIsActive(true);
        user.setRole(defaultRole);
        
        // Save user
        userRepository.save(user);
        
        // Generate JWT token
        String token = jwtService.generateToken(user.getUsername());
        
        // Return token in response
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("message", "User registered successfully");
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
}
