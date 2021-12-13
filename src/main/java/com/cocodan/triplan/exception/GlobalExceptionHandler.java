package com.cocodan.triplan.exception;

import com.cocodan.triplan.exception.common.ForbiddenException;
import com.cocodan.triplan.exception.common.NotFoundException;
import com.cocodan.triplan.exception.common.NotIncludeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        log.warn("Method Argument Not Valid.");
        Map<String, String> errorMap = new ConcurrentHashMap<>();
        exception.getBindingResult()
                .getAllErrors()
                .forEach(error -> putError(error, errorMap));

        return new ResponseEntity<>(errorMap, HttpStatus.BAD_REQUEST);
    }

    private void putError(ObjectError error, Map<String, String> errors) {
        errors.put(
                ((FieldError) error).getField(),
                error.getDefaultMessage()
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotFound(NotFoundException exception) {
        log.warn("{} Not Found. Id : {}", exception.getClazz().getSimpleName(), exception.getId());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleNotInclude(NotIncludeException exception) {
        log.warn("{} (id :{}) is not included {} (id:{})",
                exception.getPart().getSimpleName(),
                exception.getPartId(),
                exception.getEntire().getSimpleName(),
                exception.getEntireId()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleForbidden(ForbiddenException exception) {
        log.warn("{} (id :{}) can not access {} (id:{})",
                exception.getAccessor().getSimpleName(),
                exception.getAccessorId(),
                exception.getResource().getSimpleName(),
                exception.getResourceId()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

}
