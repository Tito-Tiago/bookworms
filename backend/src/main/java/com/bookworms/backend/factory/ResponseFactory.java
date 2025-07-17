package com.bookworms.backend.factory;

import com.bookworms.backend.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public final class ResponseFactory {

    private ResponseFactory() {}

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, HttpStatus status) {
        return new ResponseEntity<>(ApiResponse.success(data), status);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return success(data, HttpStatus.OK);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return success(data, HttpStatus.CREATED);
    }

    public static ResponseEntity<ApiResponse<Object>> error(HttpStatus status, String message, Map<String, String> errors) {
        return new ResponseEntity<>(ApiResponse.error(message, errors), status);
    }

    public static ResponseEntity<ApiResponse<Object>> error(HttpStatus status, String message) {
        return error(status, message, null);
    }
}