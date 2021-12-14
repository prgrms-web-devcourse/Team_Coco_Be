package com.cocodan.triplan.exception.common;

import com.cocodan.triplan.util.ExceptionMessageUtils;
import lombok.Getter;

@Getter
public class NoFriendsException extends RuntimeException {

    private Long toId;

    private Long fromId;

    public NoFriendsException(Long toId, Long fromId) {
        super(ExceptionMessageUtils.getMessage("exception.bad_request"));
        this.toId = toId;
        this.fromId = fromId;
    }
}
