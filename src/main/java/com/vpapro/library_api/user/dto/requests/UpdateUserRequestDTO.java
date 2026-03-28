package com.vpapro.library_api.user.dto.requests;

import jakarta.validation.constraints.*;

public record UpdateUserRequestDTO(

        @Size(min = 2, max = 50)
        @Pattern(regexp = "^[a-zA-Z]+$")
        String firstName,

        @Size(min = 2, max = 50)
        @Pattern(regexp = "^[a-zA-Z]+$")
        String lastName,

        @Email(message = "Invalid email format")
        String email
) {
}
