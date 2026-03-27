package com.vpapro.library_api.auth;

import com.vpapro.library_api.user.dto.requests.CreateUserRequestDTO;
import com.vpapro.library_api.auth.dto.requests.LoginRequestDTO;
import com.vpapro.library_api.auth.dto.responses.LoginResponseDTO;
import com.vpapro.library_api.user.dto.responses.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    ResponseEntity<UserResponseDTO> register(@Valid @RequestBody CreateUserRequestDTO createUserRequestDTO) {
        UserResponseDTO userResponseDTO = authService.register(createUserRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);
        return ResponseEntity.ok(loginResponseDTO);
    }
}
