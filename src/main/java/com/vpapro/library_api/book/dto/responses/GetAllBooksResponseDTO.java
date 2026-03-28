package com.vpapro.library_api.book.dto.responses;

import java.util.List;

public record GetAllBooksResponseDTO(List<BookResponseDTO> books) {
}
