package com.sideProject.PlanIT.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INVALID_PARAMETER(400, "파라미터 값을 확인해주세요."),
    RESOURCE_NOT_FOUND(404, "리소스를 찾을 수 없습니다.");;



    private final int status;
    private final String message;
}
