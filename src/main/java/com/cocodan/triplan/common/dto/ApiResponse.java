package com.cocodan.triplan.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

@Getter
public class ApiResponse<T> {

    private int code;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    private ApiResponse(int code) {
        this(code, null);
    }

    private ApiResponse(int code, T result) {
        this.code = code;
        this.result = result;
    }

    public static <T> ApiResponse<T> createApiResponse(int code) {
        return new ApiResponse<>(code);
    }

    public static <T> ApiResponse<T> createApiResponse(int code, T result) {
        return new ApiResponse<>(code, result);
    }
}
