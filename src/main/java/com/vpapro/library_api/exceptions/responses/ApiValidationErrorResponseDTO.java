package com.vpapro.library_api.exceptions.responses;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ApiValidationErrorResponseDTO(List<String> message, HttpStatus httpStatus) {
}
