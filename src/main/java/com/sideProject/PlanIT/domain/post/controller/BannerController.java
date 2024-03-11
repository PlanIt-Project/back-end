package com.sideProject.PlanIT.domain.post.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.post.dto.request.BannerRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.BannerResponseDto;
import com.sideProject.PlanIT.domain.post.entity.Banner;
import com.sideProject.PlanIT.domain.post.service.BannerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @PostMapping("/banner")
    public ApiResponse<Banner> createBanner(@ModelAttribute BannerRequestDto bannerRequestDto) {
        return ApiResponse.ok(bannerService.createBanner(bannerRequestDto));
    }

    @PutMapping("/banner/{banner_id}")
    public ApiResponse<Banner> editBanner(@PathVariable Long banner_id, @ModelAttribute BannerRequestDto bannerRequestDto) {
        return ApiResponse.ok(bannerService.editBanner(banner_id, bannerRequestDto));
    }

    @GetMapping("/admin/banner")
    public ApiResponse<List<BannerResponseDto>> findAllBanners() {
        return ApiResponse.ok(bannerService.findAllBanners());
    }

    @GetMapping("/banner")
    public ApiResponse<List<BannerResponseDto>> findAllBannersInTime() {
        return ApiResponse.ok(bannerService.findAllBannersInTime());
    }

    @GetMapping("/banner/{banner_id}")
    public ApiResponse<BannerResponseDto> findBanner(@PathVariable Long banner_id) {
        return ApiResponse.ok(bannerService.findBanner(banner_id));
    }
}
