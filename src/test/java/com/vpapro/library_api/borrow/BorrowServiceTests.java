package com.vpapro.library_api.borrow;

import com.vpapro.library_api.book.BookEntity;
import com.vpapro.library_api.book.BookService;
import com.vpapro.library_api.exceptions.AvailableCopiesExceedTotalException;
import com.vpapro.library_api.exceptions.BookIsNotAvailableException;
import com.vpapro.library_api.exceptions.BorrowNotFoundException;
import com.vpapro.library_api.user.UserEntity;
import com.vpapro.library_api.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BorrowServiceTests {

    @Mock private BorrowRepository borrowRepository;
    @Mock private UserService userService;
    @Mock private BookService bookService;
    @Mock private Authentication authentication;

    @InjectMocks private BorrowService borrowService;

    private UserEntity user;
    private BookEntity book;
    private BorrowEntity borrow;

    @BeforeEach
    void setUp() {
        user = UserEntity.builder()
                .id(1L)
                .email("test@test.com")
                .build();

        book = BookEntity.builder()
                .id(1L)
                .totalCopies(5)
                .availableCopies(3)
                .build();

        borrow = BorrowEntity.builder()
                .id(1L)
                .user(user)
                .book(book)
                .borrowDate(LocalDateTime.now())
                .build();
    }

    @Test
    void borrowBook_whenEverythingWasSetCorrectly_returnsBorrow() {
        when(bookService.findBook(1L)).thenReturn(book);
        when(userService.findCurrentUser(authentication)).thenReturn(user);
        when(borrowRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        BorrowEntity result = borrowService.borrowBook(authentication, 1L);

        assertThat(result.getBook().getAvailableCopies()).isEqualTo(2);
        assertThat(result.getUser().getEmail()).isEqualTo("test@test.com");
        assertThat(result.getDueDate()).isAfter(result.getBorrowDate());
    }

    @Test
    void borrowBook_whenAvailableCopiesIsZeroOrLess_throwsException() {
        book.setAvailableCopies(0);
        when(bookService.findBook(1L)).thenReturn(book);

        assertThatThrownBy(() -> borrowService.borrowBook(authentication, 1L))
                .isInstanceOf(BookIsNotAvailableException.class);
    }

    @Test
    void returnBook_whenEverythingWasSetCorrectly_returnBorrow() {
        when(bookService.findBook(1L)).thenReturn(book);
        when(userService.findCurrentUser(authentication)).thenReturn(user);
        when(borrowRepository.findByUserAndBookAndReturnDateIsNull(user, book)).thenReturn(Optional.ofNullable(borrow));

        BorrowEntity result = borrowService.returnBook(authentication, 1L);

        assertThat(result.getBook().getAvailableCopies()).isEqualTo(4);
        assertThat(result.getUser().getEmail()).isEqualTo("test@test.com");
        assertThat(result.getReturnDate()).isNotNull();
    }

    @Test
    void returnBook_whenBorrowNotExist_throwsException() {
        when(bookService.findBook(1L)).thenReturn(book);
        when(userService.findCurrentUser(authentication)).thenReturn(user);
        when(borrowRepository.findByUserAndBookAndReturnDateIsNull(user, book)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> borrowService.returnBook(authentication, 1L))
                .isInstanceOf(BorrowNotFoundException.class);
    }

    @Test
    void returnBook_whenAvailableCopiesExceedTotal_throwsException() {
        book.setTotalCopies(3);
        when(bookService.findBook(1L)).thenReturn(book);
        when(userService.findCurrentUser(authentication)).thenReturn(user);
        when(borrowRepository.findByUserAndBookAndReturnDateIsNull(user, book)).thenReturn(Optional.ofNullable(borrow));

        assertThatThrownBy(() -> borrowService.returnBook(authentication, 1L))
                .isInstanceOf(AvailableCopiesExceedTotalException.class);
    }

}
