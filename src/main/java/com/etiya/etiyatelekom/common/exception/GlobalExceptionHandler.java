package com.etiya.etiyatelekom.common.exception;

import com.etiya.etiyatelekom.common.exception.exceptions.CannotDeactivateAdminException;
import com.etiya.etiyatelekom.common.exception.exceptions.InvalidCredentialsException;
import com.etiya.etiyatelekom.common.exception.exceptions.PasswordsDoNotMatchException;
import com.etiya.etiyatelekom.common.exception.exceptions.InvalidTicketStatusTransitionException;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceAlreadyExistsException;
import com.etiya.etiyatelekom.common.exception.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiError> handleAlreadyExists(ResourceAlreadyExistsException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError> handleInvalidCredentials(InvalidCredentialsException ex, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler(PasswordsDoNotMatchException.class)
    public ResponseEntity<ApiError> handlePasswordsDoNotMatch(PasswordsDoNotMatchException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "Access denied", req.getRequestURI(), null);
    }

    @ExceptionHandler(CannotDeactivateAdminException.class)
    public ResponseEntity<ApiError> handleCannotDeactivateAdmin(CannotDeactivateAdminException ex, HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler(InvalidTicketStatusTransitionException.class)
    public ResponseEntity<ApiError> handleInvalidStatus(
            InvalidTicketStatusTransitionException ex,
            HttpServletRequest req
    ) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        return build(HttpStatus.BAD_REQUEST, "Validation failed", req.getRequestURI(), errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleOther(Exception ex, HttpServletRequest req) {
        log.error("Unexpected error at {}: {} - {}", req.getRequestURI(), ex.getClass().getSimpleName(), ex.getMessage(), ex);
        String msg = "Unexpected error";
        if (ex.getCause() != null) {
            log.error("Caused by: {}", ex.getCause().getMessage(), ex.getCause());
        }
        return build(HttpStatus.INTERNAL_SERVER_ERROR, msg, req.getRequestURI(), null);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String message, String path, Map<String, String> val) {
        ApiError body = ApiError.builder()
                .timestamp(OffsetDateTime.now())
                .status(status.value())
                .message(message)
                .path(path)
                .validationErrors(val)
                .build();
        return ResponseEntity.status(status).body(body);
    }
}
