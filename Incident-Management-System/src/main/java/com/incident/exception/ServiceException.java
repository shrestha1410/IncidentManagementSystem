package com.incident.exception;

import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;
    private String[] args;

    private Throwable cause;

    public ServiceException(@NotNull HttpStatus httpStatus, @NotNull String errorCode, @NotNull String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ServiceException(@NotNull HttpStatus httpStatus, @NotNull String errorCode, @NotNull String errorMessage, String[] args) {
        this(httpStatus, errorCode, errorMessage);
        this.args = args;
    }

    public ServiceException(@NotNull HttpStatus httpStatus, @NotNull String errorCode, @NotNull String errorMessage, String[] args, Throwable cause) {
        this(httpStatus, errorCode, errorMessage, args);
        this.cause = cause;
    }
}
