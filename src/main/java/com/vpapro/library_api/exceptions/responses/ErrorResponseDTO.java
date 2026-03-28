package com.vpapro.library_api.exceptions.responses;

import org.springframework.http.HttpStatus;

public record ErrorResponseDTO(String message, HttpStatus status) {
}
