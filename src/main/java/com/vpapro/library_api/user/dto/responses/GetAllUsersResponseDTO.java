package com.vpapro.library_api.user.dto.responses;

import java.util.List;

public record GetAllUsersResponseDTO(List<UserResponseDTO> users) {
}
