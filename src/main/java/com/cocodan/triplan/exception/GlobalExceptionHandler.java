package com.cocodan.triplan.exception;

import com.cocodan.triplan.common.ApiResponse;
import com.cocodan.triplan.exception.common.*;
import com.cocodan.triplan.util.ExceptionMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ApiResponse<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        log.warn("Method Argument Not Valid.", exception);
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult()
                .getAllErrors()
                .forEach(error -> putError(error, errorMap));

        return ApiResponse.fail(
                ExceptionMessageUtils.getMessage("exception.argument_not_valid"),
                errorMap,
                HttpStatus.BAD_REQUEST
        );
    }

    private void putError(ObjectError error, Map<String, String> errors) {
        errors.put(
                ((FieldError) error).getField(),
                error.getDefaultMessage()
        );
    }

    @ExceptionHandler
    public ApiResponse<Void> handleNotFound(NotFoundException exception) {
        log.warn(exception.getMessage());
        return ApiResponse.fail(ExceptionMessageUtils.getMessage("exception.not_found"), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ApiResponse<Void> handleNotInclude(NotIncludeException exception) {
        log.warn("{} (id :{}) is not included {} (id:{})",
                exception.getPart().getSimpleName(),
                exception.getPartId(),
                exception.getEntire().getSimpleName(),
                exception.getEntireId(),
                exception
        );
        return ApiResponse.fail(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ApiResponse<Void> handleForbidden(ForbiddenException exception) {
        log.warn("Member {} can not access {} {}",
                exception.getAccessorId(),
                exception.getResource().getSimpleName(),
                exception.getResourceId(),
                exception
        );
        return ApiResponse.fail(exception.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    public ApiResponse<Void> handleUniqueEmail(UniqueEmailException exception) {
        log.warn("{} Unique email : {}", exception.getClazz().getSimpleName(), exception.getEmail(), exception);
        return ApiResponse.fail(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ApiResponse<Void> handleInvalidLogin(BadCredentialsException exception) {
        log.warn("Invalid token data : {}", exception.getMessage(), exception);
        return ApiResponse.fail(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ApiResponse<Void> handleInvalidLogin(IllegalArgumentException exception) {
        log.warn("Invalid login password check : {}", exception.getMessage(), exception);
        return ApiResponse.fail(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ApiResponse<Void> handleInvalidLogin(UsernameNotFoundException exception) {
        log.warn("Invalid login email : {}", exception.getMessage(), exception);
        return ApiResponse.fail(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ApiResponse<Void> handleNoFriends(NoFriendsException exception) {
        log.warn("No Friend Exception : {}", exception.getMessage(), exception);
        return ApiResponse.fail(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }


    /* 최하단 유지 */
    @ExceptionHandler
    public ApiResponse<Void> handleException(Exception exception) {
        log.error("Unexpected error", exception);
        return ApiResponse.fail(ExceptionMessageUtils.getMessage("exception"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
