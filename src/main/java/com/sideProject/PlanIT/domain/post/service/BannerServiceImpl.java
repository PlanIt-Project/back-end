package com.sideProject.PlanIT.domain.post.service;

import com.sideProject.PlanIT.common.modules.FileHandler;
import com.sideProject.PlanIT.domain.post.dto.BannerDto;
import com.sideProject.PlanIT.domain.post.entity.Banner;
import com.sideProject.PlanIT.domain.post.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService{

    private final BannerRepository bannerRepository;

    @Autowired
    private FileHandler fileHandler;

    public Banner createBanner(BannerDto.BannerRequestDto bannerRequestDto) {
        return bannerRepository.save(Banner.builder()
                .title(bannerRequestDto.getTitle())
                .startAt(bannerRequestDto.getStartAt())
                .endAt(bannerRequestDto.getEndAt())
                .imagePath(fileHandler.saveFile(bannerRequestDto.getImage()))
                .build());
    }

    @Override
    public Banner editBanner(Long banner_id, BannerDto.BannerRequestDto bannerRequestDto) {
        Banner bannerToEdit = bannerRepository.findById(banner_id).orElseThrow(() -> new IllegalStateException("no exist id"));
        return bannerRepository.save(bannerToEdit.update(bannerRequestDto));
    }

    @Override
    public List<BannerDto.BannerResponseDto> findAllBanners() {
        List<Banner> banners = bannerRepository.findAll();
        return banners.stream()
                .map(Banner::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BannerDto.BannerResponseDto> findAllBannersInTime() {
        List<Banner> banners = bannerRepository.findByStartAtBeforeAndEndAtAfter();
        return banners.stream()
                .map(Banner::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BannerDto.BannerResponseDto findBanner(Long banner_id) {
        return Banner.toDto(bannerRepository.findById(banner_id).orElseThrow(() -> new IllegalArgumentException("no exist id")));
    }
}
