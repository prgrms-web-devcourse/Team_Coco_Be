package com.cocodan.triplan.exception;

import com.cocodan.triplan.exception.common.ForbiddenException;
import com.cocodan.triplan.exception.common.NotFoundException;
import com.cocodan.triplan.exception.common.NotIncludeException;
import com.cocodan.triplan.util.ExceptionMessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        Map<String, String> errorMap = new HashMap<>();
        exception.getBindingResult()
                .getAllErrors()
                .forEach(error -> putError(error, errorMap));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExceptionResponse.from(
                        ExceptionMessageUtils.getMessage("exception.argument_not_valid"),
                        errorMap)
        );
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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.from(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleForbidden(ForbiddenException exception) {
        log.warn("{} (id :{}) can not access {} (id:{})",
                exception.getAccessor().getSimpleName(),
                exception.getAccessorId(),
                exception.getResource().getSimpleName(),
                exception.getResourceId()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ExceptionResponse.from(exception.getMessage()));
    }

}
