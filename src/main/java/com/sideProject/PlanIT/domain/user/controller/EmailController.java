package com.sideProject.PlanIT.domain.user.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.user.dto.member.request.EmailSendReqeustDto;
import com.sideProject.PlanIT.domain.user.dto.member.request.EmailValidationRequestDto;
import com.sideProject.PlanIT.domain.user.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping
    public ApiResponse<String> mailSend(@RequestBody @Valid EmailSendReqeustDto emailSendReqeustDto) {
        return ApiResponse.ok(emailService.joinEmail(emailSendReqeustDto.getEmail()));
    }

    @PostMapping("/check")
    public ApiResponse<String> mailValidation(@RequestBody @Valid EmailValidationRequestDto emailValidationRequestDto) {
        return ApiResponse.ok(emailService.validEmail(emailValidationRequestDto));
    }

}
