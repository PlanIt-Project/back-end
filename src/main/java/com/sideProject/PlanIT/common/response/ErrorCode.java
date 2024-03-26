package com.sideProject.PlanIT.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INVALID_PARAMETER(400, "파라미터 값을 확인해주세요."),
    ALREADY_REFUNDED_PROGRAM(400, "이미 환불된 프로그램입니다."),
    ALREADY_APPROVE_PROGRAM(400, "이미 등록된 프로그램입니다."),
    ALREADY_EXIST_EMAIL(400, "이미 존재하는 이메일입니다."),
    NO_EXIST_EMAIL(400, "이메일을 입력해주세요."),
    NO_EXIST_PASSWORD(400, "비밀번호를 입력해주세요."),
    INVALID_PASSWORD(400, "비밀번호가 틀렸습니다."),
    INVALID_EMAIL_AUTH(400, "인증번호가 틀렸습니다."),
    NOT_PT(400, "PT이용권이 아닙니다."),
    NOT_YOUR_TRAINER(400, "예약 가능한 트레이너가 아닙니다."),
    ALREADY_RESERVATION(400, "이미 예약 되어있습니다."),
    SAME_PASSWORD(400, "변경 비밀번호가 같습니다."),
    INVALID_RESERVATION_TIME(400, "예약 가능한 시간이 아닙니다."),

    //401
    INVALID_ACCESS_TOKEN(401, "ACCESS TOKEN 오류"),
    INVALID_REFRESH_TOKEN(401, "REFRESH TOKEN 오류"),
    NO_AUTHORITY(401, "권한이 없습니다."),

    //404 Error Not found.
    RESOURCE_NOT_FOUND(404, "리소스를 찾을 수 없습니다."),
    PRODUCT_NOT_FOUND(404, "상품을 찾을 수 없습니다."),
    PROGRAM_NOT_FOUND(404,"프로그램을 찾을 수 없습니다"),
    REGISTRATION_NOT_FOUND(404,"등록을 찾을 수 없습니다"),
    MEMBER_NOT_FOUND(404,"회원을 찾을 수 없습니다"),
    EMPLOYEE_NOT_FOUND(404,"직원을 찾을 수 없습니다"),
    IMAGE_NOT_FOUND(404,"이미지를 찾을 수 없습니다"),
    RESERVATION_NOT_FOUND(404,"등록되지 않은 예약입니다."),
    FILE_NOT_FOUND(404,"파일을 찾을 수 없습니다"),

    NOT_SUSPEND_PROGRAM(422, "일시정지 요청이 거부되었습니다."),
    SUSPEND_REQUEST_DENIED(422, "일시정지 요청이 거부되었습니다.");


    private final int status;
    private final String message;
}
