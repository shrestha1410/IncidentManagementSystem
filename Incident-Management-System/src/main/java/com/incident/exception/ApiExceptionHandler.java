package com.incident.exception;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incident.dto.ErrorResponse;
import com.incident.utils.ErrorCodes;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestControllerAdvice
@Setter
@Slf4j
public class ApiExceptionHandler {
    @Autowired
    private ApiResponseUtils apiResponseUtils;

    @ExceptionHandler(value = ServiceException.class)
    public ResponseEntity<ErrorResponse> handleMarketplaceException(ServiceException exception) {
        if (exception.getHttpStatus() != null) {
            return ResponseEntity.status(exception.getHttpStatus()).body(apiResponseUtils
                    .buildErrorResponse(exception.getErrorCode(), exception.getErrorMessage(), exception.getArgs()));
        }
        return ResponseEntity.badRequest().body(apiResponseUtils.buildErrorResponse(exception.getErrorCode(),
                exception.getErrorMessage(), exception.getArgs()));
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponseUtils.buildErrorResponse(ErrorCodes.UN_AUTHORIZED, "Unauthorized. "+exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        BindingResult errors = e.getBindingResult();
        String errorMessage= String.join(". ", errors.getFieldErrors().stream()
                .map(fieldError -> String.join(" ",fieldError.getField(),fieldError.getDefaultMessage()))
                .collect(Collectors.toSet()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(apiResponseUtils.buildErrorResponse(
                        ErrorCodes.BAD_REQUEST,
                        HttpStatus.BAD_REQUEST.getReasonPhrase(), new String[] { errorMessage }));
    }

    @ExceptionHandler(value = HttpStatusCodeException.class)
    public ResponseEntity<ErrorResponse> handleHttpServerErrorException(HttpStatusCodeException exception) {
        ErrorResponse.ErrorResponseBuilder errorResponseBuilder=ErrorResponse.builder();
        ErrorResponse errorResponse;
        try {
            errorResponseBuilder = tryDecodeDefaultErrorMessage(exception);
            errorResponse=errorResponseBuilder.build();
            if(errorResponse.getErrorMessage().isEmpty()){
                errorResponseBuilder = tryDecodeAuthErrorMessage(exception);
            }
        }catch (Exception ignored){errorResponseBuilder.errorMessage("");}
        errorResponse = errorResponseBuilder.build();
        boolean hasErrorCode = errorResponse.getErrorCode()!=null && !errorResponse.getErrorCode().trim().isEmpty();
        boolean hasErrorDescription = errorResponse.getErrorMessage()!=null && !errorResponse.getErrorMessage().trim().isEmpty();
        String errorCode = (hasErrorCode)?errorResponse.getErrorCode():ErrorCodes.ERROR_MODULE_PREFIX+exception.getStatusCode().value();
        String errorMessage = (hasErrorDescription)?errorResponse.getErrorMessage(): exception.getStatusCode().toString();
        String[] errorArgs = (hasErrorCode && hasErrorDescription)?new String[]{""}:new String[]{errorMessage};
        return ResponseEntity.status(exception.getStatusCode())
                .body(apiResponseUtils.buildErrorResponse(errorCode,
                        errorMessage, errorArgs));
    }
    private ErrorResponse.ErrorResponseBuilder tryDecodeDefaultErrorMessage(HttpStatusCodeException exception){
        ErrorResponse.ErrorResponseBuilder errorResponseBuilder=ErrorResponse.builder();
        try {
            String responseBody = exception.getResponseBodyAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorResponse errorResponse = objectMapper.readValue(responseBody,ErrorResponse.class);
            errorResponseBuilder = errorResponse.toBuilder();
        }catch (Exception ignored){errorResponseBuilder.errorMessage("");}
        return errorResponseBuilder;
    }
    private ErrorResponse.ErrorResponseBuilder tryDecodeAuthErrorMessage(HttpStatusCodeException exception){
        ErrorResponse.ErrorResponseBuilder errorResponseBuilder = ErrorResponse.builder();
        try {
            String responseBody = exception.getResponseBodyAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String,Object> errorResponse = objectMapper.readValue(responseBody, new TypeReference<>() {
            });
            errorResponseBuilder =  Optional.ofNullable(errorResponse).map(error -> error.get("errors"))
                    .filter(errors -> List.class.isAssignableFrom(errors.getClass()))
                    .map(errors -> (List<Map<String, Object>>) errors).flatMap(errList -> errList.stream()
                            .flatMap(err -> {
                                if (err.get("message") != null && err.get("code") != null) {
                                    return Stream.of(err);
                                }
                                return Stream.empty();
                            })
                            .filter(Objects::nonNull).findFirst()
                            .map(err -> ErrorResponse.builder()
                                    .errorCode((String) err.get("code"))
                                    .errorMessage((String) err.get("message"))))
                    .orElseGet(()->Optional.ofNullable(errorResponse)
                            .map(error -> error.get("message"))
                            .map(msg -> ErrorResponse.builder().errorMessage((String) msg))
                            .orElse(ErrorResponse.builder().errorMessage("")));
        }catch (Exception ignored){errorResponseBuilder.errorMessage("");}
        return errorResponseBuilder;
    }


    @ExceptionHandler(value = Throwable.class)
    public ResponseEntity<ErrorResponse> handleDefaultException(Throwable th) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiResponseUtils.buildErrorResponse(ErrorCodes.INTERNAL_SERVER_ERROR,
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        new String[] { HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase() }));
    }

}
