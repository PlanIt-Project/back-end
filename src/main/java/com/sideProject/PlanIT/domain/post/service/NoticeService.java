package com.sideProject.PlanIT.domain.post.service;


import com.sideProject.PlanIT.domain.post.dto.NoticeDto;
import com.sideProject.PlanIT.domain.post.entity.Notice;
import com.sideProject.PlanIT.domain.post.repository.NoticeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@AllArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final FileService fileService;

    public Notice createNotice(NoticeDto.NoticeRequestDto noticeRequestDto) {
        return noticeRepository.save(Notice.builder()
                .title(noticeRequestDto.getTitle())
                .startAt(noticeRequestDto.getStartAt())
                .endAt(noticeRequestDto.getEndAt())
                .attachmentPath(fileService.saveFile(noticeRequestDto.getAttachment()))
                .imagePath(fileService.saveFile(noticeRequestDto.getImage()))
                .content(noticeRequestDto.getContent())
                .build());
    }

    public Notice editNotice(Long notice_id, NoticeDto.NoticeRequestDto noticeRequestDto) {
        Notice noticeToEdit = noticeRepository.findById(notice_id).orElseThrow(() -> new IllegalArgumentException("no exist Id"));
        fileService.saveFile(noticeRequestDto.getAttachment());
        fileService.saveFile(noticeRequestDto.getImage());
        //todo: 기존 저장된 파일 제거?

        return noticeRepository.save(noticeToEdit.update(noticeRequestDto));
    }
}
