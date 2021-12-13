package com.cocodan.triplan.exception.common;

import com.cocodan.triplan.util.ExceptionMessageUtils;
import lombok.Getter;

@Getter
public class NotIncludeException extends RuntimeException{

    private final Class<?> entire;

    private final Class<?> part;

    private final Long entireId;

    private final Long partId;

    public NotIncludeException(Class<?> entire, Class<?> part, Long entireId, Long partId) {
        super(ExceptionMessageUtils.getMessage("exception.not_include"));
        this.entire = entire;
        this.part = part;
        this.entireId = entireId;
        this.partId = partId;
    }
}
