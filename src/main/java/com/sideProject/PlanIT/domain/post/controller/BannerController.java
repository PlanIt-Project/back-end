package com.sideProject.PlanIT.domain.post.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.post.dto.request.BannerRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.BannerResponseDto;
import com.sideProject.PlanIT.domain.post.service.BannerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//todo: Banner, Notice 둘 다 Edit 발생 시 기존 첨부파일 삭제 후 다시 저장
@RestController
@AllArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @PostMapping("/admin/banner")
    public ApiResponse<String> createBanner(@ModelAttribute BannerRequestDto bannerRequestDto) {
        return ApiResponse.ok(bannerService.createBanner(bannerRequestDto));
    }

    @PutMapping("admin/banner/{banner_id}")
    public ApiResponse<String> editBanner(@PathVariable Long banner_id, @ModelAttribute BannerRequestDto bannerRequestDto) {
        return ApiResponse.ok(bannerService.editBanner(banner_id, bannerRequestDto));
    }

    @DeleteMapping("/admin/banner/{banner_id}")
    public ApiResponse<String> deleteBanner(@PathVariable Long banner_id) {
        return ApiResponse.ok(bannerService.deleteBanner(banner_id));
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
