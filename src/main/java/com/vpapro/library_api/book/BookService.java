package com.vpapro.library_api.book;

import com.vpapro.library_api.book.dto.requests.CreateBookRequestDTO;
import com.vpapro.library_api.book.dto.requests.UpdateBookRequestDTO;
import com.vpapro.library_api.exceptions.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookMapper mapper;

    public List<BookEntity> findAllBooks() {
        return bookRepository.findAll();
    }

    public BookEntity findBook(final Long id) {
        return bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
    }

    public BookEntity addBook(final CreateBookRequestDTO createBookRequestDTO) {
        if (bookRepository.existsByIsbn(createBookRequestDTO.isbn())){
            throw new BookAlreadyExistException();
        }

        return bookRepository.save(mapper.mapFromCreateBookRequestDTOToBookEntity(createBookRequestDTO));
    }

    public BookEntity updateBook(final Long id, final UpdateBookRequestDTO updateBookRequestDTO) {
        BookEntity bookFromDatabase = findBook(id);

        if (updateBookRequestDTO.title() != null) {
            bookFromDatabase.setTitle(updateBookRequestDTO.title());
        }

        if (updateBookRequestDTO.author() != null) {
            bookFromDatabase.setAuthor(updateBookRequestDTO.author());
        }

        if (updateBookRequestDTO.totalCopies() != null) {
            if (updateBookRequestDTO.totalCopies() < bookFromDatabase.getAvailableCopies() || updateBookRequestDTO.totalCopies() <= 0) {
                throw new AvailableCopiesExceedTotalException();
            }
            bookFromDatabase.setTotalCopies(updateBookRequestDTO.totalCopies());
        }

        return bookFromDatabase;
    }

    public void deleteBook(final Long id) {
        BookEntity book = findBook(id);
        bookRepository.delete(book);
    }
}
