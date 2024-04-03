package com.sideProject.PlanIT.domain.post.service;

import com.sideProject.PlanIT.domain.post.dto.request.BannerRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.BannerResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BannerService {
    String  createBanner(BannerRequestDto bannerRequestDto);
    String editBanner(Long banner_id, BannerRequestDto bannerRequestDto);
    String deleteBanner(Long banner_id);
    Page<BannerResponseDto> findAllBanners(Pageable pageable);
    Page<BannerResponseDto> findAllBannersInTime(Pageable pageable);
    BannerResponseDto findBanner(Long banner_id);
}
