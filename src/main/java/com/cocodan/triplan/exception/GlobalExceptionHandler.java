package com.cocodan.triplan.exception;

import com.cocodan.triplan.exception.common.*;
import com.cocodan.triplan.jwt.JwtAuthentication;
import com.cocodan.triplan.util.ExceptionMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        log.warn("Method Argument Not Valid.");
        loggingWarningStackTrace(exception);
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult()
                .getAllErrors()
                .forEach(error -> putError(error, errorMap));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionResponse.of(
                        ExceptionMessageUtils.getMessage("exception.argument_not_valid"),
                        errorMap)
        );
    }

    private void loggingWarningStackTrace(Exception exception) {
        log.warn("Exception {}: ", exception.getMessage(), exception);
    }

    private void putError(ObjectError error, Map<String, String> errors) {
        errors.put(
                ((FieldError) error).getField(),
                error.getDefaultMessage()
        );
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleNotFound(NotFoundException exception) {
        log.warn("{} Not Found. Id : {}", exception.getClazz().getSimpleName(), exception.getId());
        loggingWarningStackTrace(exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ExceptionResponse.from(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleNotInclude(NotIncludeException exception) {
        log.warn("{} (id :{}) is not included {} (id:{})",
                exception.getPart().getSimpleName(),
                exception.getPartId(),
                exception.getEntire().getSimpleName(),
                exception.getEntireId()
        );
        loggingWarningStackTrace(exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.from(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleForbidden(ForbiddenException exception) {
        log.warn("Member {} can not access {} {}",
                exception.getAccessorId(),
                exception.getResource().getSimpleName(),
                exception.getResourceId()
        );
        loggingWarningStackTrace(exception);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionResponse.from(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleUniqueEmail(UniqueEmailException exception) {
        log.warn("{} Unique email : {}", exception.getClazz().getSimpleName(), exception.getEmail());
        loggingWarningStackTrace(exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.from(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleInvalidLogin(BadCredentialsException exception) {
        log.warn("Invalid token data : {}", exception.getMessage());
        loggingWarningStackTrace(exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.from(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleInvalidLogin(IllegalArgumentException exception) {
        log.warn("Invalid login password check : {}", exception.getMessage());
        loggingWarningStackTrace(exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.from(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleInvalidLogin(UsernameNotFoundException exception) {
        log.warn("Invalid login email : {}", exception.getMessage());
        loggingWarningStackTrace(exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.from(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleNoFriends(NoFriendsException exception) {
        log.warn("No Friend Exception : {}", exception.getMessage());
        loggingWarningStackTrace(exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.from(exception.getMessage()));
    }


    /* 최하단 유지 */
    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        loggingErrorStackTrace(exception);

        return ResponseEntity.internalServerError().body(
                ExceptionResponse.from(
                        ExceptionMessageUtils.getMessage("exception")
                )
        );
    }

    private void loggingErrorStackTrace(Exception exception) {
        log.error("Unexpected error {}",exception.getMessage(), exception);
    }
}
