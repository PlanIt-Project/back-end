package com.sideProject.PlanIT.domain.post.service;

import com.sideProject.PlanIT.common.modules.FileHandler;
import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.domain.post.dto.request.BannerRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.BannerResponseDto;
import com.sideProject.PlanIT.domain.post.entity.Banner;
import com.sideProject.PlanIT.domain.post.repository.BannerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
public class BannerServiceTest {

    @Autowired
    BannerRepository bannerRepository;

    @Autowired
    BannerService bannerService;

    @Autowired
    FileHandler fileHandler;

    @AfterEach
    void tearDown() {
        bannerRepository.deleteAllInBatch();
    }

    Banner initInTimeBanner(String title, String imagePath) {
        return bannerRepository.save(Banner.builder()
                .title(title)
                .startAt(LocalDate.of(2001, 1, 1))
                .endAt(LocalDate.of(2050, 1, 1))
                .imagePath(imagePath)
                .build());
    }

    Banner initNotInTimeBanner(String title, String imagePath) {
        return bannerRepository.save(Banner.builder()
                .title(title)
                .startAt(LocalDate.of(2050, 1, 1))
                .endAt(LocalDate.of(2001, 1, 1))
                .imagePath(imagePath)
                .build());
    }

    MultipartFile createMockMutipartFile(String name) {
        return new MockMultipartFile("file", "test.txt", "text/plain", "test file".getBytes(StandardCharsets.UTF_8) );
    }

    @Nested
    @DisplayName("createBannerTest")
    class createBannerTest {

        @DisplayName("배너를 생성한다")
        @Test
        public void createBanner() {

            // given
            MultipartFile testImage = createMockMutipartFile("testImage");

            BannerRequestDto request = BannerRequestDto.builder()
                    .title("test Banner")
                    .startAt(LocalDate.of(2001, 1, 1))
                    .endAt(LocalDate.of(2001, 1, 1))
                    .image(testImage)
                    .build();

            // when
            bannerService.createBanner(request);

            // then
            Banner result = bannerRepository.findByTitle("test Banner").get();
            assertThat(result.getTitle()).isEqualTo("test Banner");
            assertThat(result.getStartAt()).isEqualTo(LocalDate.of(2001, 1, 1));
            assertThat(result.getEndAt()).isEqualTo(LocalDate.of(2001, 1, 1));
            assertThat(result.getImagePath()).isEqualTo(fileHandler.saveFile(testImage));
        }
    }

    @Nested
    @DisplayName("editBannerTest")
    class editBannerTest {

        @DisplayName("배너를 수정한다")
        @Test
        public void editBanner() {

            // given
            Banner testBanner = initInTimeBanner("test Banner", "testImagePath");

            MultipartFile testImage = createMockMutipartFile("testImage");

            BannerRequestDto request = BannerRequestDto.builder()
                    .title("test Banner")
                    .startAt(LocalDate.of(2001, 1, 1))
                    .endAt(LocalDate.of(2001, 1, 1))
                    .image(testImage)
                    .build();

            // when
            bannerService.editBanner(testBanner.getId(), request);

            // then
            Banner result = bannerRepository.findById(testBanner.getId()).get();
            assertThat(result.getTitle()).isEqualTo("test Banner");
            assertThat(result.getStartAt()).isEqualTo(LocalDate.of(2001, 1, 1));
            assertThat(result.getEndAt()).isEqualTo(LocalDate.of(2001, 1, 1));
            assertThat(result.getImagePath()).isEqualTo(fileHandler.saveFile(testImage));
        }

        @DisplayName("수정할 배너가 없으면 에러가 발생한다")
        @Test
        public void editBanner2() {

            // given
            Banner testBanner = initInTimeBanner("test Banner", "testImagePath");

            MultipartFile testImage = createMockMutipartFile("testImage");

            BannerRequestDto request = BannerRequestDto.builder()
                    .title("test Banner")
                    .startAt(LocalDate.of(2001, 1, 1))
                    .endAt(LocalDate.of(2001, 1, 1))
                    .image(testImage)
                    .build();

            // when, then
            assertThatThrownBy(() -> bannerService.editBanner(testBanner.getId() + 1, request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("배너를 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("deleteBannerTest")
    class deleteBannerTest {

        @DisplayName("배너를 삭제한다")
        @Test
        public void deleteBanner() {

            // given
            Banner testBanner = initInTimeBanner("test Banner", "testImagePath");

            // when
            String result = bannerService.deleteBanner(testBanner.getId());

            // then
            assertThat(result).isEqualTo("배너 삭제 완료");
            assertThat(bannerRepository.findById(testBanner.getId()).isPresent()).isFalse();
        }
    }

    @Nested
    @DisplayName("findAllBannersTest")
    class findAllBannersTest {

        @DisplayName("모든 배너를 조회한다")
        @Test
        public void findAllBanners() {

            // given
            initInTimeBanner("test Banner", "testImagePath");
            initNotInTimeBanner("test Banner2", "testImagePath2");

            Pageable pageable = PageRequest.of(0, 2);

            // when
            Page<BannerResponseDto> result = bannerService.findAllBanners(pageable);

            // then
            assertThat(result.getTotalElements()).isEqualTo(2);
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.getContent().size()).isEqualTo(2);

            assertThat(result.get().toList().get(0).getTitle()).isEqualTo("test Banner");
            assertThat(result.get().toList().get(1).getTitle()).isEqualTo("test Banner2");
        }
    }

    @Nested
    @DisplayName("findAllBannersInTimeTest")
    class findAllBannersInTimeTest {

        @DisplayName("현재 노출 시간인 배너를 조회한다")
        @Test
        public void findAllBannersInTime() {

            // given
            initInTimeBanner("test Banner", "testImagePath");
            initNotInTimeBanner("test Banner2", "testImagePath2");

            Pageable pageable = PageRequest.of(0, 2);

            // when
            Page<BannerResponseDto> result = bannerService.findAllBannersInTime(pageable);

            // then
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.getContent().size()).isEqualTo(1);
            assertThat(result.get().toList().get(0).getTitle()).isEqualTo("test Banner");
        }
    }

    @Nested
    @DisplayName("findBannerTest")
    class findBannerTest {

        @DisplayName("배너를 상세 조회한다")
        @Test
        public void findBanner() {

            // given
            Banner testBanner = initInTimeBanner("test Banner", "testImagePath");

            // when
            BannerResponseDto result = bannerService.findBanner(testBanner.getId());

            // then
            assertThat(result.getTitle()).isEqualTo("test Banner");
            assertThat(result.getStartAt()).isEqualTo(LocalDate.of(2001, 1, 1));
            assertThat(result.getEndAt()).isEqualTo(LocalDate.of(2050, 1, 1));
            assertThat(result.getImagePath()).isEqualTo("testImagePath");
        }

        @DisplayName("배너가 없으면 에러가 발생한다")
        @Test
        public void findBanner2() {

            // given
            Banner testBanner = initInTimeBanner("test Banner", "testImagePath");

            // when, then
            assertThatThrownBy(() -> bannerService.findBanner(testBanner.getId() + 1))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("배너를 찾을 수 없습니다");
        }
    }


}
