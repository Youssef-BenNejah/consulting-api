package brama.consultant_business_api.exception;

import brama.consultant_business_api.common.ApiError;
import lombok.Getter;

import java.util.List;

@Getter
public class RequestValidationException extends RuntimeException {
    private final List<ApiError> errors;

    public RequestValidationException(final List<ApiError> errors) {
        super("Request validation failed");
        this.errors = errors;
    }
}
