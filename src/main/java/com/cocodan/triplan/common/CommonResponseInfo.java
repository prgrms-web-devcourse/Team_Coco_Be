package com.cocodan.triplan.common;


import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class CommonResponseInfo {

    private final String message;

    private final int internalHttpStatusCode;

    private final LocalDateTime serverTime;

    public CommonResponseInfo(String message, HttpStatus internalHttpStatusCode) {
        this.message = message;
        this.internalHttpStatusCode = internalHttpStatusCode.value();
        this.serverTime = LocalDateTime.now();
    }
}
