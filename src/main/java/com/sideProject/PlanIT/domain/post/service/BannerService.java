package com.sideProject.PlanIT.domain.post.service;

import com.sideProject.PlanIT.domain.post.dto.request.BannerRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.BannerResponseDto;
import com.sideProject.PlanIT.domain.post.entity.Banner;

import java.util.List;

public interface BannerService {
    String  createBanner(BannerRequestDto bannerRequestDto);
    String editBanner(Long banner_id, BannerRequestDto bannerRequestDto);
    String deleteBanner(Long banner_id);
    List<BannerResponseDto> findAllBanners();
    List<BannerResponseDto> findAllBannersInTime();
    BannerResponseDto findBanner(Long banner_id);
}
