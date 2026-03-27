package com.vpapro.library_api.auth;

import com.vpapro.library_api.user.dto.requests.CreateUserRequestDTO;
import com.vpapro.library_api.auth.dto.requests.LoginRequestDTO;
import com.vpapro.library_api.auth.dto.responses.LoginResponseDTO;
import com.vpapro.library_api.security.CustomUserDetails;
import com.vpapro.library_api.security.JwtUtils;
import com.vpapro.library_api.user.UserService;
import com.vpapro.library_api.user.dto.responses.UserResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public UserResponseDTO register(final CreateUserRequestDTO createUserRequestDTO) {
        return userService.createUser(createUserRequestDTO);
    }

    public LoginResponseDTO login(final LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password())
        );

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        return new LoginResponseDTO(jwtUtils.generateToken(userDetails.getUsername()));
    }
}
