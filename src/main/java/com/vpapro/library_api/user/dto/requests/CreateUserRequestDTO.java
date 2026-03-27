package com.vpapro.library_api.user.dto.requests;

import jakarta.validation.constraints.*;

public record CreateUserRequestDTO(

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
        String email,

        @NotBlank
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
                message = "Password must contain at least 8 characters, one uppercase, one lowercase, one digit and one special character")
        String password
) {
}
