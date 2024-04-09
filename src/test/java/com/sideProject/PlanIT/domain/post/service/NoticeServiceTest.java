package com.sideProject.PlanIT.domain.post.service;

import com.sideProject.PlanIT.common.modules.FileHandler;
import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.domain.post.dto.request.NoticeRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.NoticeResponseDto;
import com.sideProject.PlanIT.domain.post.entity.Notice;
import com.sideProject.PlanIT.domain.post.repository.NoticeRepository;
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

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
public class NoticeServiceTest {

    @Autowired
    NoticeRepository noticeRepository;

    @Autowired
    NoticeService noticeService;

    @Autowired
    FileHandler fileHandler;

    @AfterEach
    void tearDown() {
        noticeRepository.deleteAllInBatch();
    }

    Notice initInTimeNotice(String title, String imagePath, String attachmentPath) {
        return noticeRepository.save(Notice.builder()
                .title(title)
                .startAt(LocalDate.of(2001, 1, 1))
                .endAt(LocalDate.of(2050, 1, 1))
                .imagePath(imagePath)
                .attachmentPath(attachmentPath)
                .content("test Notice content")
                .build());
    }

    Notice initNotInTimeNotice(String title, String imagePath, String attachmentPath) {
        return noticeRepository.save(Notice.builder()
                .title(title)
                .startAt(LocalDate.of(2050, 1, 1))
                .endAt(LocalDate.of(2001, 1, 1))
                .imagePath(imagePath)
                .attachmentPath(attachmentPath)
                .build());
    }

    MultipartFile createMockMutipartFile(String name) {
        return new MockMultipartFile(name, (byte[]) null);
    }

        @Nested
        @DisplayName("createNoticeTest")
        class createNoticeTest {

            @DisplayName("공지를 생성한다")
            @Test
            public void createNotice() {

                // given
                MultipartFile testImage = createMockMutipartFile("testImage");
                MultipartFile testAttachment = createMockMutipartFile("testAttachment");

                NoticeRequestDto request = NoticeRequestDto.builder()
                        .title("test Notice")
                        .startAt(LocalDate.of(2001, 1, 1))
                        .endAt(LocalDate.of(2001, 1, 1))
                        .image(testImage)
                        .attachment(testAttachment)
                        .content("test Notice content")
                        .build();

                // when
                noticeService.createNotice(request);

                // then
                Notice result = noticeRepository.findByTitle("test Notice").get();
                assertThat(result.getTitle()).isEqualTo("test Notice");
                assertThat(result.getStartAt()).isEqualTo(LocalDate.of(2001, 1, 1));
                assertThat(result.getEndAt()).isEqualTo(LocalDate.of(2001, 1, 1));
                assertThat(result.getImagePath()).isEqualTo(fileHandler.saveFile(testImage));
                assertThat(result.getAttachmentPath()).isEqualTo(fileHandler.saveFile(testAttachment));
                assertThat(result.getContent()).isEqualTo("test Notice content");
            }
        }

    @Nested
    @DisplayName("editNoticeTest")
    class editNoticeTest {

        @DisplayName("공지를 수정한다")
        @Test
        public void editNotice() {

            // given
            Notice testNotice = initInTimeNotice("test Notice to Edit", "testImageToEdit", "testAttachmentToEdit");

            MultipartFile testImage = createMockMutipartFile("testImage");
            MultipartFile testAttachment = createMockMutipartFile("testAttachment");

            NoticeRequestDto request = NoticeRequestDto.builder()
                    .title("test Notice")
                    .startAt(LocalDate.of(2001, 1, 1))
                    .endAt(LocalDate.of(2001, 1, 1))
                    .image(testImage)
                    .attachment(testAttachment)
                    .content("test Notice content")
                    .build();

            // when
            noticeService.editNotice(testNotice.getId(), request);

            // then
            Notice result = noticeRepository.findByTitle("test Notice").get();
            assertThat(result.getTitle()).isEqualTo("test Notice");
            assertThat(result.getStartAt()).isEqualTo(LocalDate.of(2001, 1, 1));
            assertThat(result.getEndAt()).isEqualTo(LocalDate.of(2001, 1, 1));
            assertThat(result.getImagePath()).isEqualTo(fileHandler.saveFile(testImage));
            assertThat(result.getAttachmentPath()).isEqualTo(fileHandler.saveFile(testAttachment));
            assertThat(result.getContent()).isEqualTo("test Notice content");
        }

        @DisplayName("수정할 공지가 없으면 에러가 발생한다")
        @Test
        public void editNotice2() {

            // given
            Notice testNotice = initInTimeNotice("test Notice to Edit", "testImageToEdit", "testAttachmentToEdit");

            MultipartFile testImage = createMockMutipartFile("testImage");
            MultipartFile testAttachment = createMockMutipartFile("testAttachment");

            NoticeRequestDto request = NoticeRequestDto.builder()
                    .title("test Notice")
                    .startAt(LocalDate.of(2001, 1, 1))
                    .endAt(LocalDate.of(2001, 1, 1))
                    .image(testImage)
                    .attachment(testAttachment)
                    .content("test Notice content")
                    .build();

            // when, then
            assertThatThrownBy(() -> noticeService.editNotice(testNotice.getId() + 1, request))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("공지를 찾을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("deleteNoticeTest")
    class deleteNoticeTest {

        @DisplayName("공지를 삭제한다")
        @Test
        public void deleteNotice() {

            // given
            Notice testNotice = initInTimeNotice("test Banner", "testImagePath", "testAttachmentPath");

            // when
            String result = noticeService.deleteNotice(testNotice.getId());

            // then
            assertThat(result).isEqualTo("공지 삭제 완료");
            assertThat(noticeRepository.findById(testNotice.getId()).isPresent()).isFalse();
        }
    }

    @Nested
    @DisplayName("findAllNoticesTest")
    class findAllNoticesTest {

        @DisplayName("모든 공지를 조회한다")
        @Test
        public void findAllNotices() {

            // given
            initInTimeNotice("test Notice", "testImagePath", "testAttachmentPath");
            initInTimeNotice("test Notice2", "testImagePath2", "testAttachmentPath2");

            Pageable pageable = PageRequest.of(0, 2);

            // when
            Page<NoticeResponseDto> result = noticeService.findAllNotices(pageable);

            // then
            assertThat(result.getTotalElements()).isEqualTo(2);
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.getContent().size()).isEqualTo(2);

            assertThat(result.get().toList().get(0).getTitle()).isEqualTo("test Notice");
            assertThat(result.get().toList().get(1).getTitle()).isEqualTo("test Notice2");
        }
    }

    @Nested
    @DisplayName("findAllNoticesInTimeTest")
    class findAllNoticesInTimeTest {

        @DisplayName("현재 노출 시간인 공지를 조회한다")
        @Test
        public void findAllNoticesInTime() {

            // given
            initInTimeNotice("test Notice", "testImagePath", "testAttachmentPath");
            initNotInTimeNotice("test Notice2", "testImagePath2", "testAttachmentPath2");

            Pageable pageable = PageRequest.of(0, 2);

            // when
            Page<NoticeResponseDto> result = noticeService.findAllNoticesInTime(pageable);

            // then
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.getContent().size()).isEqualTo(1);

            assertThat(result.get().toList().get(0).getTitle()).isEqualTo("test Notice");
        }
    }

    @Nested
    @DisplayName("findNoticeTest")
    class findNoticeTest {

        @DisplayName("공지를 상세조회한다")
        @Test
        public void findNotice() {

            // given
            Notice testNotice = initInTimeNotice("test Notice", "testImagePath", "testAttachmentPath");

            // when
            NoticeResponseDto result = noticeService.findNotice(testNotice.getId());

            // then
            assertThat(result.getTitle()).isEqualTo("test Notice");
            assertThat(result.getStartAt()).isEqualTo(LocalDate.of(2001, 1, 1));
            assertThat(result.getEndAt()).isEqualTo(LocalDate.of(2050, 1, 1));
            assertThat(result.getImagePath()).isEqualTo("testImagePath");
            assertThat(result.getAttachmentPath()).isEqualTo("testAttachmentPath");
            assertThat(result.getContent()).isEqualTo("test Notice content");
        }

        @DisplayName("공지가 없으면 에러가 발생한다")
        @Test
        public void findNotice2() {

            // given
            Notice testNotice = initInTimeNotice("test Notice", "testImagePath", "testAttachmentPath");

            // when, then
            assertThatThrownBy(() -> noticeService.findNotice(testNotice.getId() + 1))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("공지를 찾을 수 없습니다");
        }
    }
}
