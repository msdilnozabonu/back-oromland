package uz.oromland.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uz.oromland.payload.AuthDTO;
import uz.oromland.payload.RegisterDTO;
import uz.oromland.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    @ResponseBody /// bu nega kerak?
    public ResponseEntity<?> login(@RequestBody AuthDTO authDTO) {
        return authService.login(authDTO);
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO) {
        return authService.register(registerDTO);
    }

}
