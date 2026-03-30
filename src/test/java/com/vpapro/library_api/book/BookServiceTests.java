package com.vpapro.library_api.book;

import com.vpapro.library_api.book.dto.requests.CreateBookRequestDTO;
import com.vpapro.library_api.book.dto.requests.UpdateBookRequestDTO;
import com.vpapro.library_api.exceptions.AvailableCopiesExceedTotalException;
import com.vpapro.library_api.exceptions.BookAlreadyExistException;
import com.vpapro.library_api.exceptions.BookNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper mapper;

    @InjectMocks
    private BookService bookService;

    private BookEntity book;

    @BeforeEach
    void setUp() {
        book = BookEntity.builder()
                .id(1L)
                .title("Book")
                .author("Author")
                .isbn("123")
                .totalCopies(5)
                .availableCopies(3)
                .build();
    }

    @Test
    void findBook_whenBookExists_returnsBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookEntity result = bookService.findBook(1L);
        assertThat(result).isEqualTo(book);
    }

    @Test
    void findBook_whenBookNotFound_throwsException() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findBook(1L))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    void addBook_whenIsbnNotExists_returnsBook() {
        CreateBookRequestDTO dto = new CreateBookRequestDTO("Book", "Author", "123", 5);

        when(bookRepository.existsByIsbn("123")).thenReturn(false);
        when(mapper.mapFromCreateBookRequestDTOToBookEntity(dto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);

        BookEntity result = bookService.addBook(dto);
        assertThat(result).isEqualTo(book);
    }

    @Test
    void addBook_whenIsbnExists_throwsException() {
        CreateBookRequestDTO dto = new CreateBookRequestDTO("Book", "Author", "123", 5);

        when(bookRepository.existsByIsbn("123")).thenReturn(true);

        assertThatThrownBy(() -> bookService.addBook(dto))
                .isInstanceOf(BookAlreadyExistException.class);
    }

    @Test
    void updateBook_whenAllFieldsProvided_updatesAllFields() {
        UpdateBookRequestDTO dto = new UpdateBookRequestDTO("New Book", "New Author", 10);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookEntity result = bookService.updateBook(1L, dto);

        assertThat(result.getTitle()).isEqualTo("New Book");
        assertThat(result.getAuthor()).isEqualTo("New Author");
        assertThat(result.getTotalCopies()).isEqualTo(10);
    }

    @Test
    void updateBook_whenTitleFieldProvided_updatesTitleOnly() {
        UpdateBookRequestDTO dto = new UpdateBookRequestDTO("New Book", null, null);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookEntity result = bookService.updateBook(1L, dto);

        assertThat(result.getTitle()).isEqualTo("New Book");
        assertThat(result.getAuthor()).isEqualTo("Author");
        assertThat(result.getTotalCopies()).isEqualTo(5);
    }

    @Test
    void updateBook_whenAuthorFieldProvided_updatesAuthorOnly() {
        UpdateBookRequestDTO dto = new UpdateBookRequestDTO(null, "New Author", null);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookEntity result = bookService.updateBook(1L, dto);

        assertThat(result.getTitle()).isEqualTo("Book");
        assertThat(result.getAuthor()).isEqualTo("New Author");
        assertThat(result.getTotalCopies()).isEqualTo(5);
    }

    @Test
    void updateBook_whenTotalCopiesFieldProvided_updatesTotalCopiesOnly() {
        UpdateBookRequestDTO dto = new UpdateBookRequestDTO(null, null, 10);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookEntity result = bookService.updateBook(1L, dto);

        assertThat(result.getTitle()).isEqualTo("Book");
        assertThat(result.getAuthor()).isEqualTo("Author");
        assertThat(result.getTotalCopies()).isEqualTo(10);
    }

    @Test
    void updateBook_whenNoFieldsProvided_updatesNothing() {
        UpdateBookRequestDTO dto = new UpdateBookRequestDTO(null, null, null);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookEntity result = bookService.updateBook(1L, dto);

        assertThat(result.getTitle()).isEqualTo("Book");
        assertThat(result.getAuthor()).isEqualTo("Author");
        assertThat(result.getTotalCopies()).isEqualTo(5);
    }

    @Test
    void updateBook_whenTotalCopiesLessThanAvailable_throwsException() {
        UpdateBookRequestDTO dto = new UpdateBookRequestDTO(null, null, 1);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> bookService.updateBook(1L, dto))
                .isInstanceOf(AvailableCopiesExceedTotalException.class);
    }

    @Test
    void updateBook_whenTotalCopiesLessThanZero_throwsException() {
        UpdateBookRequestDTO dto = new UpdateBookRequestDTO(null, null, -1);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> bookService.updateBook(1L, dto))
                .isInstanceOf(AvailableCopiesExceedTotalException.class);
    }
}
