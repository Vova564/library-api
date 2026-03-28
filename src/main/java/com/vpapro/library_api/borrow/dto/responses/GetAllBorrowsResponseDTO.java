package com.vpapro.library_api.borrow.dto.responses;

import java.util.List;

public record GetAllBorrowsResponseDTO(List<BorrowResponseDTO> borrows) {
}
