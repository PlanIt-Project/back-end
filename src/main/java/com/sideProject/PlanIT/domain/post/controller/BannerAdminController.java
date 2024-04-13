package com.sideProject.PlanIT.domain.post.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.domain.post.dto.request.BannerRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.BannerResponseDto;
import com.sideProject.PlanIT.domain.post.service.BannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/banner")
@RequiredArgsConstructor
public class BannerAdminController {
    private final BannerService bannerService;

    @PostMapping
    public ApiResponse<String> createBanner(
            @Valid @ModelAttribute BannerRequestDto request,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            // 유효성 검사 실패 시, 사용자에게 에러를 보여주는 로직
            throw new CustomException(result.getAllErrors().toString(), ErrorCode.INVALID_PARAMETER);
        }
        return ApiResponse.ok(bannerService.createBanner(request));
    }

    @PutMapping("/{banner_id}")
    public ApiResponse<String> editBanner(@PathVariable Long banner_id, @Valid @ModelAttribute BannerRequestDto request, BindingResult result) {
        if (result.hasErrors()) {
            // 유효성 검사 실패 시, 사용자에게 에러를 보여주는 로직
            throw new CustomException(result.getAllErrors().toString(), ErrorCode.INVALID_PARAMETER);
        }
        return ApiResponse.ok(bannerService.editBanner(banner_id, request));
    }

    @DeleteMapping("/{banner_id}")
    public ApiResponse<String> deleteBanner(@PathVariable Long banner_id) {
        return ApiResponse.ok(bannerService.deleteBanner(banner_id));
    }

    @GetMapping
    public ApiResponse<Page<BannerResponseDto>> findAllBanners(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ApiResponse.ok(bannerService.findAllBanners(pageable));
    }
}
