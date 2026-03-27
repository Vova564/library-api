package com.vpapro.library_api.user;


import com.vpapro.library_api.user.dto.requests.CreateUserRequestDTO;
import com.vpapro.library_api.user.dto.responses.GetAllUsersResponseDTO;
import com.vpapro.library_api.user.dto.responses.UserResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class UserMapper {

    private PasswordEncoder encoder;

    public UserResponseDTO mapFromUserEntityToUserResponseDTO(UserEntity entity) {
        return UserResponseDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .role(entity.getRole())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public GetAllUsersResponseDTO mapFromUserEntitiesToGetAllUsersResponseDTO(List<UserEntity> entities) {
        List<UserResponseDTO> getAllUsersResponseDTO = entities.stream()
                .map(this::mapFromUserEntityToUserResponseDTO)
                .collect(Collectors.toList());
        return new GetAllUsersResponseDTO(getAllUsersResponseDTO);
    }

    public UserEntity mapFromCreateUserRequestDTOToUserEntity(CreateUserRequestDTO requestDTO) {
        return UserEntity.builder()
                .firstName(requestDTO.firstName())
                .lastName(requestDTO.lastName())
                .email(requestDTO.email())
                .passwordHash(encoder.encode(requestDTO.password()))
                .email(requestDTO.email())
                .role(UserRole.USER)
                .build();
    }
}