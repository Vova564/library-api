package com.vpapro.library_api.book.dto.requests;

import jakarta.validation.constraints.*;

public record CreateBookRequestDTO(
        
        @NotBlank
        String title,

        @NotBlank
        @Size(max = 100)
        String author,

        @NotBlank
        @Size(max = 20)
        @Pattern(regexp = "^(?=(?:[^0-9]*[0-9]){10}(?:(?:[^0-9]*[0-9]){3})?$)[\\d-]+$")
        String isbn,

        @NotNull
        @Min(1)
        Integer totalCopies
) {
}
