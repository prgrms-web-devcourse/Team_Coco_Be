package com.cocodan.triplan.exception.common;

import com.cocodan.triplan.util.ExceptionMessageUtils;
import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException{

    private Class<?> resource;

    private Class<?> accessor;

    private Long resourceId;

    private Long accessorId;

    public ForbiddenException(Class<?> resource, Class<?> accessor, Long resourceId, Long accessorId) {
        super(ExceptionMessageUtils.getMessage("exception.forbidden"));
        this.resource = resource;
        this.accessor = accessor;
        this.resourceId = resourceId;
        this.accessorId = accessorId;
    }
}
