package com.vpapro.library_api.exceptions;

import com.vpapro.library_api.exceptions.responses.ApiValidationErrorResponseDTO;
import com.vpapro.library_api.exceptions.responses.ErrorResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Log4j2
class ExceptionsHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ApiValidationErrorResponseDTO handleValidationExceptions(MethodArgumentNotValidException exception) {
        log.warn("Caught MethodArgumentNotValidException: {}", exception.getMessage());
        List<String> message = getErrorsFromException(exception);
        return new ApiValidationErrorResponseDTO(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDTO handleInvalidRequestBody(HttpMessageNotReadableException exception) {
        log.warn("Caught HttpMessageNotReadableException: {}", exception.getMessage());
        return new ErrorResponseDTO("Invalid input: " + exception.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponseDTO handleUserNotFoundException(UserNotFoundException exception) {
        log.warn("Caught UserNotFoundException: {}", exception.getMessage());
        return new ErrorResponseDTO(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserAlreadyExistException.class)
    public ErrorResponseDTO handleUserAlreadyExistException(UserAlreadyExistException exception) {
        log.warn("Caught UserAlreadyExistException: {}", exception.getMessage());
        return new ErrorResponseDTO(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponseDTO handleTaskNotFoundException(AccessDeniedException exception) {
        log.warn("Caught AccessDeniedException: {}", exception.getMessage());
        return new ErrorResponseDTO(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BookNotFoundException.class)
    public ErrorResponseDTO handleBookNotFoundException(BookNotFoundException exception) {
        log.warn("Caught BookNotFoundException: {}", exception.getMessage());
        return new ErrorResponseDTO(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(BookAlreadyExistException.class)
    public ErrorResponseDTO handleBookAlreadyExistException(BookAlreadyExistException exception) {
        log.warn("Caught BookAlreadyExistException: {}", exception.getMessage());
        return new ErrorResponseDTO(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BookIsNotAvailableException.class)
    public ErrorResponseDTO handleBookIsNotAvailableException(BookIsNotAvailableException exception) {
        log.warn("Caught BookIsNotAvailableException: {}", exception.getMessage());
        return new ErrorResponseDTO(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AvailableCopiesExceedTotalException.class)
    public ErrorResponseDTO handleAvailableCopiesExceedTotalException(AvailableCopiesExceedTotalException exception) {
        log.warn("Caught AvailableCopiesExceedTotalException: {}", exception.getMessage());
        return new ErrorResponseDTO(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BorrowNotFoundException.class)
    public ErrorResponseDTO handleBorrowNotFoundException(BorrowNotFoundException exception) {
        log.warn("Caught BorrowNotFoundException: {}", exception.getMessage());
        return new ErrorResponseDTO(exception.getMessage(), HttpStatus.NOT_FOUND);
    }


    private List<String> getErrorsFromException(MethodArgumentNotValidException exception) {
        return exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
    }
}
