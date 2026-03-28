package com.vpapro.library_api.book.dto.requests;

import jakarta.validation.constraints.Size;

public record UpdateBookRequestDTO(

        String title,

        @Size(max = 100)
        String author,

        Integer totalCopies
) {
}
