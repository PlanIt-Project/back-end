package com.sideProject.PlanIT.domain.post.service;


import com.sideProject.PlanIT.domain.post.dto.NoticeDto;
import com.sideProject.PlanIT.domain.post.entity.Notice;

import java.util.List;


public interface NoticeService {
    Notice createNotice(NoticeDto.NoticeRequestDto noticeRequestDto);
    Notice editNotice(Long notice_id, NoticeDto.NoticeRequestDto noticeRequestDto);
    List<NoticeDto.NoticeResponseDto> findAllNotices();
    List<NoticeDto.NoticeResponseDto> findAllNoticesInTime();
    public NoticeDto.NoticeResponseDto findNotice(Long notice_id);
}
