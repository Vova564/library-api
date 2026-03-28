package com.vpapro.library_api.book;

import com.vpapro.library_api.book.dto.requests.CreateBookRequestDTO;
import com.vpapro.library_api.book.dto.responses.BookResponseDTO;
import com.vpapro.library_api.book.dto.responses.GetAllBooksResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {

    public BookResponseDTO mapFromBookEntityToBookResponseDTO(BookEntity entity) {
        return BookResponseDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .author(entity.getAuthor())
                .isbn(entity.getIsbn())
                .totalCopies(entity.getTotalCopies())
                .availableCopies(entity.getAvailableCopies())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public GetAllBooksResponseDTO mapFromBookEntitiesToGetAllBooksResponseDTO(List<BookEntity> entities) {
        List<BookResponseDTO> getAllBooksResponseDTO = entities.stream()
                .map(this::mapFromBookEntityToBookResponseDTO)
                .collect(Collectors.toList());
        return new GetAllBooksResponseDTO(getAllBooksResponseDTO);
    }

    public BookEntity mapFromCreateBookRequestDTOToBookEntity(CreateBookRequestDTO requestDTO) {
        return BookEntity.builder()
                .title(requestDTO.title())
                .author(requestDTO.author())
                .isbn(requestDTO.isbn())
                .totalCopies(requestDTO.totalCopies())
                .availableCopies(requestDTO.totalCopies())
                .build();
    }
}
