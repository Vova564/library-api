package com.vpapro.library_api.borrow;

import com.vpapro.library_api.borrow.dto.responses.BorrowResponseDTO;
import com.vpapro.library_api.borrow.dto.responses.GetAllBorrowsResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrows")
@AllArgsConstructor
public class BorrowController {

    private final BorrowService borrowService;
    private final BorrowMapper mapper;

    @GetMapping("/me")
    ResponseEntity<GetAllBorrowsResponseDTO> findCurrentUserBorrows(Authentication authentication) {
        List<BorrowEntity> userBorrows = borrowService.findCurrentUserBorrows(authentication);
        return ResponseEntity.ok(mapper.mapFromBorrowEntitiesToGetAllBorrowsResponseDTO(userBorrows));
    }

    @PostMapping("/{bookId}")
    ResponseEntity<BorrowResponseDTO> borrowBook(Authentication authentication, @PathVariable Long bookId) {
        BorrowEntity borrowEntity = borrowService.borrowBook(authentication, bookId);
        return ResponseEntity.ok(mapper.mapFromBorrowEntityToBorrowResponseDTO(borrowEntity));
    }

    @PatchMapping("/{bookId}/return")
    ResponseEntity<BorrowResponseDTO> returnBook(Authentication authentication, @PathVariable Long bookId) {
        BorrowEntity borrowEntity = borrowService.returnBook(authentication, bookId);
        return ResponseEntity.ok(mapper.mapFromBorrowEntityToBorrowResponseDTO(borrowEntity));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    ResponseEntity<GetAllBorrowsResponseDTO> findAllBorrows() {
        List<BorrowEntity> userBorrows = borrowService.findAllBorrows();
        return ResponseEntity.ok(mapper.mapFromBorrowEntitiesToGetAllBorrowsResponseDTO(userBorrows));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    ResponseEntity<GetAllBorrowsResponseDTO> findUserBorrows(@PathVariable Long userId) {
        List<BorrowEntity> userBorrows = borrowService.findUserBorrows(userId);
        return ResponseEntity.ok(mapper.mapFromBorrowEntitiesToGetAllBorrowsResponseDTO(userBorrows));
    }
}
