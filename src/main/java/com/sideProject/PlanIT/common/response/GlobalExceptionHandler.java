package com.sideProject.PlanIT.common.response;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.UnexpectedTypeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import java.security.SignatureException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @InitBinder
    protected void handleBindException(WebDataBinder binder) {
        if(binder.getBindingResult().hasErrors()) {
            throw new CustomException("파라미터 값이 잘못되었습니다",ErrorCode.INVALID_PARAMETER);
        }
    }

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

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ApiResponse<?>> handelMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(400)
                .body(
                        ApiResponse.error(
                                400,
                                ex.getMessage()));
    }


    @ExceptionHandler(value = IllegalStateException.class)
    protected ResponseEntity<ApiResponse<?>> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.status(400)
                .body(
                        ApiResponse.error(
                                400,
                                "파라미터 값이 정상적으로 들어오지 않았습니다."));
    }
}
