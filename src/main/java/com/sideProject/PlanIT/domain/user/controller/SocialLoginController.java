package com.sideProject.PlanIT.domain.user.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.user.dto.member.response.JwtResponseDto;
import com.sideProject.PlanIT.domain.user.service.SocialLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SocialLoginController {

    private final SocialLoginService socialLoginService;

    @GetMapping("/login/oauth2/code/naver")
    public ApiResponse<JwtResponseDto> loginToNaver(@RequestParam("code") String code) throws Exception {
        return ApiResponse.ok(socialLoginService.naverSocialLogin(code));
    }

    @GetMapping("/login/oauth2/code/google")
    public ApiResponse<JwtResponseDto> loginToGoogle(@RequestParam("code") String code) throws Exception {
        return ApiResponse.ok(socialLoginService.googleSocialLogin(code));
    }
}
