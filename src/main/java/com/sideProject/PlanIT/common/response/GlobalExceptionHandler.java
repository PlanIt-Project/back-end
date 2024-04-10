package com.sideProject.PlanIT.common.response;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.security.SignatureException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<ApiResponse<?>> handleCustomException(CustomException ex) {
        return ResponseEntity.status(ex.getErrorCode().getStatus())
                .body(ApiResponse.error(ex.getErrorCode()));
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiResponse<?>> handleHttpException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(400)
                .body(ApiResponse.error(ErrorCode.INVALID_INPUT));
    }

    @ExceptionHandler(value = BindException.class)
    protected ResponseEntity<ApiResponse<?>> handleHttpException(BindException ex) {
        return ResponseEntity.status(400)
                .body(
                    ApiResponse.error(
                        400,
                        ex.getBindingResult().getAllErrors().get(0).getDefaultMessage()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    protected ResponseEntity<ApiResponse<?>> handleNoHandlerFoundException(NoHandlerFoundException e,
                                                                          HttpServletRequest request) {
        return ResponseEntity.status(404)
                .body(ApiResponse.error(404, "잘못된 페이지 입니다."));
    }
}
