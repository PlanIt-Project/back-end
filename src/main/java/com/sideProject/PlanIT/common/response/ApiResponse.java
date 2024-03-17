package com.sideProject.PlanIT.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonPropertyOrder({"code", "message", "data"})
public class ApiResponse<T> {
    private int code;
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> ok(T data) {return new ApiResponse<>(HttpStatus.OK.value(), HttpStatus.OK.name(), data);}

    public static ApiResponse<?> error(ErrorCode errorCode) {return new ApiResponse<>(errorCode.getStatus(), errorCode.getMessage(),null);}
}
