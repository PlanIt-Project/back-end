package com.sideProject.PlanIT.domain.post.service;


import com.sideProject.PlanIT.domain.post.dto.request.NoticeRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.NoticeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface NoticeService {
    String createNotice(NoticeRequestDto noticeRequestDto);
    String editNotice(Long notice_id, NoticeRequestDto noticeRequestDto);
    String deleteNotice(Long notice_id);
    Page<NoticeResponseDto> findAllNotices(Pageable pageable);
    Page<NoticeResponseDto> findAllNoticesInTime(Pageable pageable);
    public NoticeResponseDto findNotice(Long notice_id);
}
