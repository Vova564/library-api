package com.vpapro.library_api.book;

import com.vpapro.library_api.book.dto.requests.CreateBookRequestDTO;
import com.vpapro.library_api.book.dto.requests.UpdateBookRequestDTO;
import com.vpapro.library_api.book.dto.responses.BookResponseDTO;
import com.vpapro.library_api.book.dto.responses.GetAllBooksResponseDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@AllArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookMapper mapper;

    @GetMapping
    ResponseEntity<GetAllBooksResponseDTO> findAllBooks() {
        List<BookEntity> books = bookService.findAllBooks();
        return ResponseEntity.ok(mapper.mapFromBookEntitiesToGetAllBooksResponseDTO(books));
    }

    @GetMapping("/{id}")
    ResponseEntity<BookResponseDTO> findBook(@PathVariable Long id) {
        BookEntity book = bookService.findBook(id);
        return ResponseEntity.ok(mapper.mapFromBookEntityToBookResponseDTO(book));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    ResponseEntity<BookResponseDTO> addBook(@Valid @RequestBody CreateBookRequestDTO createBookRequestDTO) {
        BookEntity book = bookService.addBook(createBookRequestDTO);
        return ResponseEntity.ok(mapper.mapFromBookEntityToBookResponseDTO(book));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    ResponseEntity<BookResponseDTO> updateBook(@PathVariable Long id, @Valid @RequestBody UpdateBookRequestDTO updateBookRequestDTO) {
        BookEntity book = bookService.updateBook(id, updateBookRequestDTO);
        return ResponseEntity.ok(mapper.mapFromBookEntityToBookResponseDTO(book));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
