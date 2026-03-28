package com.vpapro.library_api.book.dto.responses;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BookResponseDTO(Long id, String title, String author, String isbn, Integer totalCopies, Integer availableCopies, LocalDateTime createdAt, LocalDateTime updatedAt) {}
