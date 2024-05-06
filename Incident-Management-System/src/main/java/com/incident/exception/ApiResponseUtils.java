package com.incident.exception;

import com.incident.dto.ErrorResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.stream.Collectors;

@Component
public class ApiResponseUtils {
    private final MessageSource apiErrors;

    public ApiResponseUtils(MessageSource apiErrors) {
        this.apiErrors = apiErrors;
    }

    public ErrorResponse buildErrorResponse(String errorCode, String defaultMessage, String[] args) {
        String errorDescription =  apiErrors.getMessage(errorCode,args,defaultMessage,
                    LocaleContextHolder.getLocale());
        return ErrorResponse.builder().errorCode(errorCode).errorMessage(errorDescription).build();
    }

    public ErrorResponse buildErrorResponse(String errorCode, String defaultMessage) {
        return buildErrorResponse(errorCode, defaultMessage, null);
    }
    public static void invokeValidator(Validator validator, Object target, BindingResult errors) throws ServiceException {
        ValidationUtils.invokeValidator(validator,target,errors);
        if(errors.hasErrors()){
            String errorMessage= String.join(". ", errors.getFieldErrors().stream().map(fieldError -> String.join(" ",fieldError.getField(),fieldError.getDefaultMessage())).collect(Collectors.toSet()));
            throw new ServiceException(HttpStatus.BAD_REQUEST, " "," ",new String[]{errorMessage});
        }
    }
}
