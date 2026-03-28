package com.vpapro.library_api.borrow.dto.responses;

import com.vpapro.library_api.user.dto.responses.UserResponseDTO;
import com.vpapro.library_api.book.dto.responses.BookResponseDTO;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BorrowResponseDTO(Long id, UserResponseDTO user, BookResponseDTO book, LocalDateTime borrowDate, LocalDateTime dueDate, LocalDateTime returnDate ) {
}
