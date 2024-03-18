package com.sideProject.PlanIT.domain.user.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.user.dto.member.response.JwtResponseDto;
import com.sideProject.PlanIT.domain.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @GetMapping("/refresh")
    public ApiResponse<JwtResponseDto> refreshAccessToken(@RequestHeader("Authorization") String authorizationHeader) {
        log.info(authorizationHeader);
        return ApiResponse.ok(authService.refreshAccessToken(authorizationHeader));
    }
}
