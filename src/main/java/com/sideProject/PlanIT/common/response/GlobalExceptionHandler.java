package com.sideProject.PlanIT.common.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<ApiResponse<?>> handleCustomException(CustomException ex) {
        return ResponseEntity.status(ex.getErrorCode().getStatus())
                .body(ApiResponse.error(ex.getErrorCode()));
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiResponse<?>> handleHttpMessageException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(400)
                .body(ApiResponse.error(ErrorCode.INVALID_INPUT));
    }
}
