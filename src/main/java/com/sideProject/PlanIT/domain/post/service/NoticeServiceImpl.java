package com.sideProject.PlanIT.domain.post.service;


import com.sideProject.PlanIT.common.modules.FileHandler;
import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.domain.post.dto.request.NoticeRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.NoticeResponseDto;
import com.sideProject.PlanIT.domain.post.entity.Notice;
import com.sideProject.PlanIT.domain.post.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
        return "공지 생성 완료";
    }

    @Override
    public String editNotice(Long notice_id, NoticeRequestDto noticeRequestDto) {
        Notice noticeToEdit = noticeRepository.findById(notice_id).orElseThrow(() -> new CustomException(ErrorCode.NOTICE_NOT_FOUND));
        String attachmentPath = fileHandler.saveFile(noticeRequestDto.getAttachment());
        String imagePath = fileHandler.saveFile(noticeRequestDto.getImage());
        //todo: 기존 저장된 파일 제거?

        noticeRepository.save(noticeToEdit.update(noticeRequestDto, attachmentPath, imagePath));

        return "공지 수정 완료";
    }

    @Override
    public String deleteNotice(Long notice_id) {
        noticeRepository.deleteById(notice_id);
        return "공지 삭제 완료";
    }

    @Override
    public Page<NoticeResponseDto> findAllNotices(Pageable pageable) {
        Page<Notice> notices = noticeRepository.findAll(pageable);
        return notices.map(NoticeResponseDto::of);
    }

    @Override
    public Page<NoticeResponseDto> findAllNoticesInTime(Pageable pageable) {
        Page<Notice> notices = noticeRepository.findByStartAtBeforeAndEndAtAfter(pageable);
        return notices.map(NoticeResponseDto::of);
    }

    public NoticeResponseDto findNotice(Long notice_id) {
        return NoticeResponseDto.of(noticeRepository.findById(notice_id).orElseThrow(() -> new CustomException(ErrorCode.NOTICE_NOT_FOUND)));
    }


}
