package brama.consultant_business_api.handler;


import brama.consultant_business_api.common.ApiError;
import brama.consultant_business_api.common.ApiResponse;
import brama.consultant_business_api.exception.BusinessException;
import brama.consultant_business_api.exception.EntityNotFoundException;
import brama.consultant_business_api.exception.ErrorCode;
import brama.consultant_business_api.exception.RequestValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j

public class ApplicationExceptionHandler {
    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleRequestValidationException(final RequestValidationException ex) {
        log.info("Request validation exception {}", ex.getMessage());
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getErrors()));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(final BusinessException ex) {
        final ApiError error = ApiError.builder()
                .code(ex.getErrorCode().getCode())
                .message(ex.getMessage())
                .build();
        log.info("Business exception {}", ex.getMessage());
        log.debug(ex.getMessage(), ex);
        return ResponseEntity
                .status(ex.getErrorCode().getStatus() != null ? ex.getErrorCode().getStatus() : HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(List.of(error)));
    }
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ApiResponse<Void>> handleDisabledException(final DisabledException ex) {
        final ApiError error = ApiError.builder()
                .code(ErrorCode.ERR_USER_DISABLED.getCode())
                .message(ErrorCode.ERR_USER_DISABLED.getDefaultMessage())
                .build();
        log.info("Disabled exception {}", ex.getMessage());
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(ErrorCode.ERR_USER_DISABLED.getStatus())
                .body(ApiResponse.error(List.of(error)));
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentialsException(final BadCredentialsException ex) {

        log.info("Bad credentials exception {}", ex.getMessage());
        log.debug(ex.getMessage(), ex);
        final ApiError error = ApiError.builder()
                .code(ErrorCode.BAD_CREDENTIALS.getCode())
                .message(ErrorCode.BAD_CREDENTIALS.getDefaultMessage())
                .build();
        return ResponseEntity.status(ErrorCode.BAD_CREDENTIALS.getStatus())
                .body(ApiResponse.error(List.of(error)));
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleUsernameNotFoundException(final UsernameNotFoundException ex) {
        final ApiError error = ApiError.builder()
                .code(ErrorCode.USERNAME_NOT_FOUND.getCode())
                .message(ErrorCode.USERNAME_NOT_FOUND.getDefaultMessage())
                .build();
        log.info("Username not found exception {}", ex.getMessage());
        log.debug(ex.getMessage(), ex);
        return new ResponseEntity<>(ApiResponse.error(List.of(error)), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEntityNotFoundException(final EntityNotFoundException ex) {
        log.info("Entity not found exception {}", ex.getMessage());
        log.debug(ex.getMessage(), ex);
        final ApiError error = ApiError.builder()
                .code("ENTITY_NOT_FOUND")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(ApiResponse.error(List.of(error)), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(final Exception ex) {
        log.error("Unhandled exception", ex);
        final ApiError error = ApiError.builder()
                .code(ErrorCode.INTERNAL_EXCEPTION.getCode())
                .message(ErrorCode.INTERNAL_EXCEPTION.getDefaultMessage())
                .build();

        return ResponseEntity.status(ErrorCode.INTERNAL_EXCEPTION.getStatus())
                .body(ApiResponse.error(List.of(error)));
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        final List<ApiError> validationErrors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            final String fieldName = ((FieldError) error).getField();
            validationErrors.add(ApiError.builder()
                    .field(fieldName)
                    .code(error.getDefaultMessage())
                    .message(error.getDefaultMessage())
                    .build());
        });
        log.info("Validation exception {}", ex.getMessage());
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(validationErrors));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(final IllegalArgumentException ex) {
        final ApiError error = ApiError.builder()
                .code(ErrorCode.INVALID_REQUEST.getCode())
                .message(ex.getMessage())
                .build();
        log.info("Illegal argument exception {}", ex.getMessage());
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(List.of(error)));
    }

    @ExceptionHandler({DuplicateKeyException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiResponse<Void>> handleDuplicateKey(final Exception ex) {
        final ApiError error = ApiError.builder()
                .code("DUPLICATE_KEY")
                .message("Duplicate key error")
                .build();
        log.info("Duplicate key exception {}", ex.getMessage());
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(List.of(error)));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(final MethodArgumentTypeMismatchException ex) {
        final ApiError error = ApiError.builder()
                .code(ErrorCode.INVALID_REQUEST.getCode())
                .message("Invalid value for parameter: " + ex.getName())
                .field(ex.getName())
                .build();
        log.info("Type mismatch {}", ex.getMessage());
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(List.of(error)));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotReadable(final HttpMessageNotReadableException ex) {
        final ApiError error = ApiError.builder()
                .code(ErrorCode.INVALID_REQUEST.getCode())
                .message("Malformed JSON request")
                .build();
        log.info("Message not readable {}", ex.getMessage());
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(List.of(error)));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParam(final MissingServletRequestParameterException ex) {
        final ApiError error = ApiError.builder()
                .code(ErrorCode.INVALID_REQUEST.getCode())
                .message("Missing required parameter: " + ex.getParameterName())
                .field(ex.getParameterName())
                .build();
        log.info("Missing request parameter {}", ex.getMessage());
        log.debug(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(List.of(error)));
    }

}

