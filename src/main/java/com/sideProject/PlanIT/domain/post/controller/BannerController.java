package com.sideProject.PlanIT.domain.post.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.domain.post.dto.BannerDto;
import com.sideProject.PlanIT.domain.post.service.BannerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @PostMapping("/banner")
    public ApiResponse<?> createBanner(@ModelAttribute BannerDto.BannerRequestDto bannerRequestDto) {
        try {
            return ApiResponse.ok(bannerService.createBanner(bannerRequestDto));
        } catch (Exception e) {
            return ApiResponse.error(ErrorCode.INVALID_PARAMETER);
        }
    }

    @PutMapping("/banner/{banner_id}")
    public ApiResponse<?> editBanner(@PathVariable Long banner_id, @ModelAttribute BannerDto.BannerRequestDto bannerRequestDto) {
        try {
            return ApiResponse.ok(bannerService.editBanner(banner_id, bannerRequestDto));
        } catch (Exception e) {
            return ApiResponse.error(ErrorCode.INVALID_PARAMETER);
        }
    }

    @GetMapping("/admin/banner")
    public ApiResponse<?> findAllBanners() {
        try {
            return ApiResponse.ok(bannerService.findAllBanners());
        } catch (Exception e) {
            return ApiResponse.error(ErrorCode.RESOURCE_NOT_FOUND);
        }
    }

    @GetMapping("/banner")
    public ApiResponse<?> findAllBannersInTime() {
        try {
            return ApiResponse.ok(bannerService.findAllBannersInTime());
        } catch (Exception e) {
            return ApiResponse.error(ErrorCode.RESOURCE_NOT_FOUND);
        }
    }

    @GetMapping("/banner/{banner_id}")
    public ApiResponse<?> findBanner(@PathVariable Long banner_id) {
        try {
            return ApiResponse.ok(bannerService.findBanner(banner_id));
        } catch (Exception e) {
            return ApiResponse.error(ErrorCode.INVALID_PARAMETER);
        }
    }
}
