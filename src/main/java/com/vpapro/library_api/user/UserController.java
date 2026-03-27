package com.vpapro.library_api.user;

import com.vpapro.library_api.user.dto.requests.UpdateUserRequestDTO;
import com.vpapro.library_api.user.dto.responses.GetAllUsersResponseDTO;
import com.vpapro.library_api.user.dto.responses.UserResponseDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper mapper;

    @GetMapping("/me")
    ResponseEntity<UserResponseDTO> findCurrentUser(Authentication authentication) {
        UserEntity userResponseDTO = userService.findCurrentUser(authentication);
        return ResponseEntity.ok(mapper.mapFromUserEntityToUserResponseDTO(userResponseDTO));
    }

    @PatchMapping("/me")
    ResponseEntity<UserResponseDTO> updateCurrentUser(Authentication authentication, @Valid @RequestBody UpdateUserRequestDTO updateUserRequestDTO) {
        UserEntity userResponseDTO = userService.updateCurrentUser(authentication, updateUserRequestDTO);
        return ResponseEntity.ok(mapper.mapFromUserEntityToUserResponseDTO(userResponseDTO));
    }

    @DeleteMapping("/me")
    ResponseEntity<Void> deleteCurrentUser(Authentication authentication) {
        userService.deleteCurrentUser(authentication);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    ResponseEntity<GetAllUsersResponseDTO> findAllUsers() {
        List<UserEntity> getAllUsersResponseDTO = userService.findAllUsers();
        return ResponseEntity.ok(mapper.mapFromUserEntitiesToGetAllUsersResponseDTO(getAllUsersResponseDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    ResponseEntity<UserResponseDTO> findUser(@PathVariable Long id) {
        UserEntity userResponseDTO = userService.findUser(id);
        return ResponseEntity.ok(mapper.mapFromUserEntityToUserResponseDTO(userResponseDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDTO updateUserRequestDTO) {
        UserEntity userResponseDTO = userService.updateUser(id, updateUserRequestDTO);
        return ResponseEntity.ok(mapper.mapFromUserEntityToUserResponseDTO(userResponseDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
