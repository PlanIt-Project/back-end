package com.sideProject.PlanIT.domain.post.entity;

import com.sideProject.PlanIT.domain.post.dto.request.NoticeRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String title;

    @Column
    private LocalDate startAt;

    @Column
    private LocalDate endAt;

    @Column
    private String attachmentPath;

    @Column
    private String imagePath;

    @Column
    private String content;

    @Builder
    public Notice(String title, LocalDate startAt, LocalDate endAt, String attachmentPath, String imagePath, String content) {
        this.title = title;
        this.startAt = startAt;
        this.endAt = endAt;
        this.attachmentPath = attachmentPath;
        this.imagePath = imagePath;
        this.content = content;
    }

    public Notice update(NoticeRequestDto noticeRequestDto) {
        this.title = noticeRequestDto.getTitle();
        this.startAt = noticeRequestDto.getStartAt();
        this.endAt = noticeRequestDto.getEndAt();
        if (noticeRequestDto.getImage() != null) {
            this.imagePath = noticeRequestDto.getImage().getOriginalFilename();
        }
        if (noticeRequestDto.getAttachment() != null) {
            this.attachmentPath = noticeRequestDto.getAttachment().getOriginalFilename();
        }
        this.content = noticeRequestDto.getContent();

        return this;
    }
}
