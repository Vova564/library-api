package com.vpapro.library_api.user.dto.requests;

import jakarta.validation.constraints.*;

public record UpdateUserRequestDTO(
        @NotBlank
        @Size(min = 2, max = 50)
        @Pattern(regexp = "^[a-zA-Z]+$")
        String firstName,

        @NotBlank
        @Size(min = 2, max = 50)
        @Pattern(regexp = "^[a-zA-Z]+$")
        String lastName,

        @NotBlank
        @Email(message = "Invalid email format")
        String email
) {
}
