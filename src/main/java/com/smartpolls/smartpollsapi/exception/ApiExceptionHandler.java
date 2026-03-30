package com.smartpolls.smartpollsapi.exception;

import com.smartpolls.smartpollsapi.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFound(NoSuchElementException ex) {
        return error(ex.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse usernameExists(UsernameAlreadyExistsException ex) {
        return error(ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse invalidCredentials(InvalidCredentialsException ex) {
        return error(ex.getMessage());
    }

    @ExceptionHandler(PollOwnershipException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse pollNotOwner(PollOwnershipException ex) {
        return error(ex.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse unauthorized(UnauthorizedException ex) {
        return error(ex.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequest(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException validationException) {
            FieldError firstError = validationException.getBindingResult().getFieldErrors().stream().findFirst().orElse(null);
            if (firstError != null) {
                return error(firstError.getDefaultMessage());
            }
        }
        return error("Invalid request");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse generic(Exception ex) {
        return error("Internal server error");
    }

    private ErrorResponse error(String message) {
        return new ErrorResponse(message, Instant.now());
    }
}
