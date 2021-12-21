package com.cocodan.triplan.exception.common;

import com.cocodan.triplan.util.ExceptionMessageUtils;
import lombok.Getter;

@Getter
public class UniqueEmailException extends RuntimeException{

    private final Class<?> clazz;
    private final String email;

    public UniqueEmailException(Class<?> clazz, String email) {
        super(ExceptionMessageUtils.getMessage("exception.unique_email"));
        this.clazz = clazz;
        this.email = email;
    }
}
