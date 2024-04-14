package com.sideProject.PlanIT.domain.post.entity;

import com.sideProject.PlanIT.domain.post.dto.request.BannerRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class BannerTest {

    @Nested
    @DisplayName("updateTest")
    public class updateTest{

        @DisplayName("Banner 정보를 수정한다")
        @Test
        public void update() {

            // given
            Banner banner = Banner.builder()
                    .title("test")
                    .startAt(LocalDate.of(2001, 1, 1))
                    .endAt(LocalDate.of(2001, 1, 2))
                    .imagePath("/image/testImg.png")
                    .build();

            BannerRequestDto bannerRequestDto = BannerRequestDto.builder()
                    .title("test1")
                    .startAt(LocalDate.of(2021, 1, 1))
                    .endAt(LocalDate.of(2021, 1, 2))
                    .build();
            String newImagePath = "/image/testImg2.png";

            // when
            banner.update(bannerRequestDto, newImagePath);

            // given
            assertThat(banner.getTitle()).isEqualTo("test1");
            assertThat(banner.getStartAt()).isEqualTo(LocalDate.of(2021, 1, 1));
            assertThat(banner.getEndAt()).isEqualTo(LocalDate.of(2021, 1, 2));
            assertThat(banner.getImagePath()).isEqualTo("/image/testImg2.png");
        }
    }
}
