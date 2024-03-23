package com.sideProject.PlanIT.domain.user.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.user.dto.member.response.JwtResponseDto;
import com.sideProject.PlanIT.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/refresh")
    public ApiResponse<JwtResponseDto> refreshAccessToken(@RequestHeader("Authorization") String authorizationHeader) {
        return ApiResponse.ok(authService.refreshAccessToken(authorizationHeader));
    }
}
