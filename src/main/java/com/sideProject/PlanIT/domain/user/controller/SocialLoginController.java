package com.sideProject.PlanIT.domain.user.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.user.dto.member.response.JwtResponseDto;
import com.sideProject.PlanIT.domain.user.service.SocialLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


//todo: 프론트와 협의 후 수정 및 api 엔드 포인트 변경
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class SocialLoginController {

    private final SocialLoginService socialLoginService;

    @GetMapping("/naver")
    public ApiResponse<String> naverLoginFormURI() {
        return ApiResponse.ok(socialLoginService.getNaverLoginURI());
    }

    @GetMapping("/google")
    public ApiResponse<String> googleLoginFormURI() {
        return ApiResponse.ok(socialLoginService.getGoogleLoginURI());
    }
    @GetMapping("/oauth2/code/naver")
    public ApiResponse<JwtResponseDto> loginToNaver(@RequestParam("code") String code) throws Exception {
        return ApiResponse.ok(socialLoginService.naverSocialLogin(code));
    }

    @GetMapping("/oauth2/code/google")
    public ApiResponse<JwtResponseDto> loginToGoogle(@RequestParam("code") String code) throws Exception {
        return ApiResponse.ok(socialLoginService.googleSocialLogin(code));
    }
}
