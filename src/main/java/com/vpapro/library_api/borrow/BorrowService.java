package com.vpapro.library_api.borrow;

import com.vpapro.library_api.book.BookEntity;
import com.vpapro.library_api.book.BookService;
import com.vpapro.library_api.exceptions.AvailableCopiesExceedTotalException;
import com.vpapro.library_api.exceptions.BookIsNotAvailableException;
import com.vpapro.library_api.exceptions.BorrowNotFoundException;
import com.vpapro.library_api.user.UserEntity;
import com.vpapro.library_api.user.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BorrowService {

    private final BorrowRepository borrowRepository;
    private final UserService userService;
    private final BookService bookService;

    public List<BorrowEntity> findCurrentUserBorrows(final Authentication authentication) {
        UserEntity user = userService.findCurrentUser(authentication);
        return borrowRepository.findAllByUser(user);
    }

    @Transactional
    public BorrowEntity borrowBook(final Authentication authentication, final Long bookId) {
        BookEntity book = bookService.findBook(bookId);

        if (book.getAvailableCopies() <= 0) {
            throw new BookIsNotAvailableException();
        }

        UserEntity user = userService.findCurrentUser(authentication);
        LocalDateTime now = LocalDateTime.now();

        book.setAvailableCopies(book.getAvailableCopies() - 1);

        BorrowEntity borrow = BorrowEntity.builder()
                .user(user)
                .book(book)
                .borrowDate(now)
                .dueDate(now.plusDays(14))
                .build();

        return borrowRepository.save(borrow);
    }

    @Transactional
    public BorrowEntity returnBook(final Authentication authentication, final Long bookId) {
        UserEntity user = userService.findCurrentUser(authentication);
        BookEntity book = bookService.findBook(bookId);

        BorrowEntity borrowFromDatabase = borrowRepository.findByUserAndBookAndReturnDateIsNull(user, book)
                .orElseThrow(BorrowNotFoundException::new);

        if ((book.getAvailableCopies() + 1) > book.getTotalCopies()) {
            throw new AvailableCopiesExceedTotalException();
        }

        book.setAvailableCopies(book.getAvailableCopies() + 1);
        borrowFromDatabase.setReturnDate(LocalDateTime.now());

        return borrowFromDatabase;
    }

    public List<BorrowEntity> findAllBorrows() {
        return borrowRepository.findAll();
    }

    public List<BorrowEntity> findUserBorrows(final Long userId) {
        UserEntity user = userService.findUser(userId);
        return borrowRepository.findAllByUser(user);
    }
}
