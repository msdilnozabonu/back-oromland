package uz.oromland.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.oromland.payload.AuthDTO;
import uz.oromland.payload.RegisterDTO;

@Service
public interface AuthService {

    /**
     * Method to handle user login.
     *
     * @param authDTO the authentication data transfer object containing username and password
     * @return a response entity containing the authentication result
     */
    ResponseEntity<?> login(AuthDTO authDTO);

    /**
     * Method to handle user registration.
     *
     * @param registerDTO the registration data transfer object containing user details
     * @return a response entity containing the registration result with JWT token
     */
    ResponseEntity<?> register(RegisterDTO registerDTO);

}
