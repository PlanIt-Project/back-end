package com.sideProject.PlanIT.domain.post.entity;

import com.sideProject.PlanIT.domain.post.dto.request.BannerRequestDto;
import com.sideProject.PlanIT.domain.post.dto.request.NoticeRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class NoticeTest {

    @Nested
    @DisplayName("updateTest")
    public class updateTest{

        @DisplayName("Notice 정보를 수정한다")
        @Test
        public void update() {

            // given
            Notice notice = Notice.builder()
                    .title("test")
                    .startAt(LocalDate.of(2001, 1, 1))
                    .endAt(LocalDate.of(2001, 1, 2))
                    .attachmentPath("/file/testAttachment.txt")
                    .imagePath("/image/testImg.png")
                    .content("test notice")
                    .build();

            NoticeRequestDto noticeRequestDto = NoticeRequestDto.builder()
                    .title("test1")
                    .startAt(LocalDate.of(2021, 1, 1))
                    .endAt(LocalDate.of(2021, 1, 2))
                    .content("test notice1")
                    .build();

            String newAttachmentPath = "/file/testAttachment2.txt";
            String newImagePath = "/image/testImg2.png";

            // when
            notice.update(noticeRequestDto, newAttachmentPath, newImagePath);

            // given
            assertThat(notice.getTitle()).isEqualTo("test1");
            assertThat(notice.getStartAt()).isEqualTo(LocalDate.of(2021, 1, 1));
            assertThat(notice.getEndAt()).isEqualTo(LocalDate.of(2021, 1, 2));
            assertThat(notice.getImagePath()).isEqualTo("/image/testImg2.png");
            assertThat(notice.getAttachmentPath()).isEqualTo("/file/testAttachment2.txt");
            assertThat(notice.getContent()).isEqualTo("test notice1");

        }
    }
}
