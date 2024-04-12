package com.sideProject.PlanIT.domain.post.service;

import com.sideProject.PlanIT.common.modules.FileHandler;
import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.domain.post.dto.request.BannerRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.BannerResponseDto;
import com.sideProject.PlanIT.domain.post.entity.Banner;
import com.sideProject.PlanIT.domain.post.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BannerServiceImpl implements BannerService{

    private final BannerRepository bannerRepository;

    @Autowired
    private FileHandler fileHandler;

    @Transactional
    public String createBanner(BannerRequestDto bannerRequestDto) {
        if(bannerRequestDto.getImage() == null || bannerRequestDto.getImage().isEmpty()) {
            throw new CustomException("이미지가 null 입니다.",ErrorCode.EMPTY_IMAGE);
        }

        bannerRepository.save(Banner.builder()
            .title(bannerRequestDto.getTitle())
            .startAt(bannerRequestDto.getStartAt())
            .endAt(bannerRequestDto.getEndAt())
            .imagePath(fileHandler.saveFile(bannerRequestDto.getImage()))
            .build());
        return "배너 생성 완료";
    }

    @Override
    public String editBanner(Long banner_id, BannerRequestDto bannerRequestDto) {
        Banner bannerToEdit = bannerRepository.findById(banner_id).orElseThrow(() -> new CustomException(ErrorCode.BANNER_NOT_FOUND));
        String imagePath = fileHandler.saveFile(bannerRequestDto.getImage());
        bannerRepository.save(bannerToEdit.update(bannerRequestDto, imagePath));
        return "배너 수정 완료";
    }

    @Override
    public String deleteBanner(Long banner_id) {
        bannerRepository.deleteById(banner_id);
        return "배너 삭제 완료";
    }

    @Override
    public Page<BannerResponseDto> findAllBanners(Pageable pageable) {
        Page<Banner> banners = bannerRepository.findAll(pageable);
        return banners.map(BannerResponseDto::of);
    }

    @Override
    public Page<BannerResponseDto> findAllBannersInTime(Pageable pageable) {
        Page<Banner> banners = bannerRepository.findByStartAtBeforeAndEndAtAfter(pageable);
        return banners.map(BannerResponseDto::of);
    }

    @Override
    public BannerResponseDto findBanner(Long banner_id) {
        return BannerResponseDto.of(bannerRepository.findById(banner_id).orElseThrow(() -> new CustomException(ErrorCode.BANNER_NOT_FOUND)));
    }
}
