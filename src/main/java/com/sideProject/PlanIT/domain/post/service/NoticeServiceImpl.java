package com.sideProject.PlanIT.domain.post.service;


import com.sideProject.PlanIT.common.modules.FileHandler;
import com.sideProject.PlanIT.domain.post.dto.request.NoticeRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.NoticeResponseDto;
import com.sideProject.PlanIT.domain.post.entity.Notice;
import com.sideProject.PlanIT.domain.post.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

    private final NoticeRepository noticeRepository;
  
    @Autowired
    private FileHandler fileHandler;

    @Override
    public Notice createNotice(NoticeRequestDto noticeRequestDto) {
        return noticeRepository.save(Notice.builder()
                .title(noticeRequestDto.getTitle())
                .startAt(noticeRequestDto.getStartAt())
                .endAt(noticeRequestDto.getEndAt())
                .attachmentPath(fileHandler.saveFile(noticeRequestDto.getAttachment()))
                .imagePath(fileHandler.saveFile(noticeRequestDto.getImage()))
                .content(noticeRequestDto.getContent())
                .build());
    }

    @Override
    public Notice editNotice(Long notice_id, NoticeRequestDto noticeRequestDto) {
        Notice noticeToEdit = noticeRepository.findById(notice_id).orElseThrow(() -> new IllegalArgumentException("no exist Id"));
        fileHandler.saveFile(noticeRequestDto.getAttachment());
        fileHandler.saveFile(noticeRequestDto.getImage());
        //todo: 기존 저장된 파일 제거?

        return noticeRepository.save(noticeToEdit.update(noticeRequestDto));
    }

    @Override
    public List<NoticeResponseDto> findAllNotices() {
        List<Notice> notices = noticeRepository.findAll();
        return notices.stream()
                .map(Notice::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeResponseDto> findAllNoticesInTime() {
        List<Notice> notices = noticeRepository.findByStartAtBeforeAndEndAtAfter();
        return notices.stream()
                .map(Notice::toDto)
                .collect(Collectors.toList());
    }

    public NoticeResponseDto findNotice(Long notice_id) {
        return Notice.toDto(noticeRepository.findById(notice_id).orElseThrow(() -> new IllegalArgumentException("no exist Id")));
    }


}
