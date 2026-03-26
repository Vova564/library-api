package com.vpapro.library_api.borrow;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vpapro.library_api.book.BookEntity;
import com.vpapro.library_api.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "borrows")
@Setter
@Getter
@NoArgsConstructor
public class BorrowEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",
                nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "book_id",
                nullable = false)
    private BookEntity book;

    @Column(nullable = false)
    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime borrowDate;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dueDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime returnDate;
}
