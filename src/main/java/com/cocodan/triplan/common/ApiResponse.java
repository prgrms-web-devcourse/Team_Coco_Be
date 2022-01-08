 package com.cocodan.triplan.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

 @Getter
public class ApiResponse<T> {

    private CommonResponseInfo common;

    private T data;

    private ApiResponse(CommonResponseInfo common) {
        this.common = common;
    }

    private ApiResponse(CommonResponseInfo common, T data) {
        this.common = common;
        this.data = data;
    }

    public static <Void> ApiResponse<Void> ok() {
        CommonResponseInfo common = new CommonResponseInfo("OK", HttpStatus.OK);
        return new ApiResponse<>(common);
    }

    public static <T> ApiResponse<T> ok(T data) {
        CommonResponseInfo common = new CommonResponseInfo("OK", HttpStatus.OK);
        return new ApiResponse<>(common, data);
    }

    public static <T> ApiResponse<T> created(T data) {
        CommonResponseInfo common = new CommonResponseInfo("CREATED", HttpStatus.CREATED);
        return new ApiResponse<>(common, data);
    }

     public static ApiResponse<Map<String, String>> fail(String message, Map<String, String> errors, HttpStatus statusCode) {
         CommonResponseInfo common = new CommonResponseInfo(message, statusCode);
         return new ApiResponse<>(common, errors);
     }

     public static ApiResponse<Void> fail(String message, HttpStatus statusCode) {
         CommonResponseInfo common = new CommonResponseInfo(message, statusCode);
         return new ApiResponse<>(common);
     }
}
