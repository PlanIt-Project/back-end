package com.sideProject.PlanIT.domain.post.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.post.dto.request.BannerRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.BannerResponseDto;
import com.sideProject.PlanIT.domain.post.service.BannerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/admin/banner")
@Slf4j
@RequiredArgsConstructor
public class BannerAdminController {
    private final BannerService bannerService;

    @PostMapping
    public ApiResponse<String> createBanner(@ModelAttribute BannerRequestDto bannerRequestDto
    ) {
        log.info(bannerRequestDto.getTitle());
        return ApiResponse.ok(bannerService.createBanner(bannerRequestDto));
    }

    @PutMapping("/{banner_id}")
    public ApiResponse<String> editBanner(@PathVariable Long banner_id, @ModelAttribute BannerRequestDto bannerRequestDto) {
        return ApiResponse.ok(bannerService.editBanner(banner_id, bannerRequestDto));
    }

    @DeleteMapping("/{banner_id}")
    public ApiResponse<String> deleteBanner(@PathVariable Long banner_id) {
        return ApiResponse.ok(bannerService.deleteBanner(banner_id));
    }

    @GetMapping
    public ApiResponse<List<BannerResponseDto>> findAllBanners() {
        return ApiResponse.ok(bannerService.findAllBanners());
    }
}
