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
    public String createNotice(NoticeRequestDto noticeRequestDto) {
        noticeRepository.save(Notice.builder()
            .title(noticeRequestDto.getTitle())
            .startAt(noticeRequestDto.getStartAt())
            .endAt(noticeRequestDto.getEndAt())
            .attachmentPath(fileHandler.saveFile(noticeRequestDto.getAttachment()))
            .imagePath(fileHandler.saveFile(noticeRequestDto.getImage()))
            .content(noticeRequestDto.getContent())
            .build());
        return "생성 완료";
    }

    @Override
    public String editNotice(Long notice_id, NoticeRequestDto noticeRequestDto) {
        Notice noticeToEdit = noticeRepository.findById(notice_id).orElseThrow(() -> new IllegalArgumentException("no exist Id"));
        fileHandler.saveFile(noticeRequestDto.getAttachment());
        fileHandler.saveFile(noticeRequestDto.getImage());
        //todo: 기존 저장된 파일 제거?

        noticeRepository.save(noticeToEdit.update(noticeRequestDto));

        return "수정 완료";
    }

    @Override
    public String deleteNotice(Long notice_id) {
        noticeRepository.deleteById(notice_id);
        return "삭제 완료";
    }

    @Override
    public List<NoticeResponseDto> findAllNotices() {
        List<Notice> notices = noticeRepository.findAll();
        return notices.stream()
                .map(NoticeResponseDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public List<NoticeResponseDto> findAllNoticesInTime() {
        List<Notice> notices = noticeRepository.findByStartAtBeforeAndEndAtAfter();
        return notices.stream()
                .map(NoticeResponseDto::of)
                .collect(Collectors.toList());
    }

    public NoticeResponseDto findNotice(Long notice_id) {
        return NoticeResponseDto.of(noticeRepository.findById(notice_id).orElseThrow(() -> new IllegalArgumentException("no exist Id")));
    }


}
