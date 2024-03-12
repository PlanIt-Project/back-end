package com.sideProject.PlanIT.domain.post.service;

import com.sideProject.PlanIT.domain.post.dto.request.BannerRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.BannerResponseDto;
import com.sideProject.PlanIT.domain.post.entity.Banner;

import java.util.List;

public interface BannerService {
    Banner createBanner(BannerRequestDto bannerRequestDto);
    Banner editBanner(Long banner_id, BannerRequestDto bannerRequestDto);
    List<BannerResponseDto> findAllBanners();
    List<BannerResponseDto> findAllBannersInTime();
    BannerResponseDto findBanner(Long banner_id);
}
