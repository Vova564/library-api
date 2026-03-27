package com.vpapro.library_api.user.dto.responses;

import com.vpapro.library_api.user.UserRole;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserResponseDTO(Long id, String firstName, String lastName, String email, UserRole role, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
