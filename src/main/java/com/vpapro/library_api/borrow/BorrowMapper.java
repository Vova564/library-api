package com.vpapro.library_api.borrow;

import com.vpapro.library_api.book.BookMapper;
import com.vpapro.library_api.borrow.dto.responses.BorrowResponseDTO;
import com.vpapro.library_api.borrow.dto.responses.GetAllBorrowsResponseDTO;
import com.vpapro.library_api.user.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class BorrowMapper {

    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public BorrowResponseDTO mapFromBorrowEntityToBorrowResponseDTO(BorrowEntity entity) {
        return BorrowResponseDTO.builder()
                .id(entity.getId())
                .user(userMapper.mapFromUserEntityToUserResponseDTO(entity.getUser()))
                .book(bookMapper.mapFromBookEntityToBookResponseDTO(entity.getBook()))
                .borrowDate(entity.getBorrowDate())
                .dueDate(entity.getDueDate())
                .returnDate(entity.getReturnDate())
                .build();
    }

    public GetAllBorrowsResponseDTO mapFromBorrowEntitiesToGetAllBorrowsResponseDTO(List<BorrowEntity> entities) {
        List<BorrowResponseDTO> getAllBorrowsResponseDTO = entities.stream()
                .map(this::mapFromBorrowEntityToBorrowResponseDTO)
                .collect(Collectors.toList());
        return new GetAllBorrowsResponseDTO(getAllBorrowsResponseDTO);
    }
}
