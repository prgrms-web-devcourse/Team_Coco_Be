package com.cocodan.triplan.exception.common;

import com.cocodan.triplan.util.ExceptionMessageUtils;
import lombok.Getter;

@Getter
public class ForbiddenException extends RuntimeException {

    private Class<?> resource;

    private Long resourceId;

    private Long accessorId;

    public ForbiddenException(Class<?> resource, Long resourceId, Long accessorId) {
        super(ExceptionMessageUtils.getMessage(
                "exception.forbidden",
                new Object[]{ExceptionMessageUtils.getMessage(resource.getSimpleName())}
        ));
        this.resource = resource;
        this.resourceId = resourceId;
        this.accessorId = accessorId;
    }
}
