package com.sideProject.PlanIT.domain.post.service;


import com.sideProject.PlanIT.domain.post.dto.request.NoticeRequestDto;
import com.sideProject.PlanIT.domain.post.dto.response.NoticeResponseDto;
import com.sideProject.PlanIT.domain.post.entity.Notice;

import java.util.List;


public interface NoticeService {
    String createNotice(NoticeRequestDto noticeRequestDto);
    String editNotice(Long notice_id, NoticeRequestDto noticeRequestDto);
    String deleteNotice(Long notice_id);
    List<NoticeResponseDto> findAllNotices();
    List<NoticeResponseDto> findAllNoticesInTime();
    public NoticeResponseDto findNotice(Long notice_id);
}
