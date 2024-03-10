package com.sideProject.PlanIT.domain.post.service;

import com.sideProject.PlanIT.domain.post.dto.BannerDto;
import com.sideProject.PlanIT.domain.post.entity.Banner;

import java.util.List;

public interface BannerService {
    Banner createBanner(BannerDto.BannerRequestDto bannerRequestDto);
    Banner editBanner(Long banner_id, BannerDto.BannerRequestDto bannerRequestDto);
    List<BannerDto.BannerResponseDto> findAllBanners();
    List<BannerDto.BannerResponseDto> findAllBannersInTime();
    BannerDto.BannerResponseDto findBanner(Long banner_id);
}
