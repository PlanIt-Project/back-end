package com.sideProject.PlanIT.domain.post.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.post.dto.response.BannerResponseDto;
import com.sideProject.PlanIT.domain.post.service.BannerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//todo: Banner, Notice 둘 다 Edit 발생 시 기존 첨부파일 삭제 후 다시 저장
@RestController
@RequiredArgsConstructor
@RequestMapping("/banner")
public class BannerController {

    private final BannerService bannerService;

    @GetMapping
    public ApiResponse<Page<BannerResponseDto>> findAllBannersInTime(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ApiResponse.ok(bannerService.findAllBannersInTime(pageable));
    }

    @GetMapping("/{banner_id}")
    public ApiResponse<BannerResponseDto> findBanner(@PathVariable Long banner_id) {
        return ApiResponse.ok(bannerService.findBanner(banner_id));
    }
}
