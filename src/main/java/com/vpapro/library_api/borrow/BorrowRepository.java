package com.vpapro.library_api.borrow;

import com.vpapro.library_api.book.BookEntity;
import com.vpapro.library_api.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRepository extends JpaRepository<BorrowEntity, Long> {

    Optional<BorrowEntity> findByUserAndBookAndReturnDateIsNull(UserEntity user, BookEntity book);

    List<BorrowEntity> findAllByUser(UserEntity user);

}
