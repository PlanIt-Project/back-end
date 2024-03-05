package com.sideProject.PlanIT.common.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value =  CustomException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ApiResponse<?> handleCustomException(CustomException ex) {
        log.error("{} : {}", ex.getClass(), ex.getMessage());
        return ApiResponse.error(ex.getErrorCode());
    }
}
