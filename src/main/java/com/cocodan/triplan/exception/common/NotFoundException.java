package com.cocodan.triplan.exception.common;

import com.cocodan.triplan.util.ExceptionMessageUtils;
import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private final Class<?> clazz;
    private final Long id;

    public NotFoundException(Class<?> clazz, Long id) {
        super(ExceptionMessageUtils.getMessage("exception.not_found"));
        this.clazz = clazz;
        this.id = id;
    }
}
