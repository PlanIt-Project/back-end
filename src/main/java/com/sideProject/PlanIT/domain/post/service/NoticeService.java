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
import java.util.List;


public interface NoticeService {
    Notice createNotice(NoticeDto.NoticeRequestDto noticeRequestDto);
    Notice editNotice(Long notice_id, NoticeDto.NoticeRequestDto noticeRequestDto);
    List<NoticeDto.NoticeResponseDto> findAllNotices();
    List<NoticeDto.NoticeResponseDto> findAllNoticesInTime();
    public NoticeDto.NoticeResponseDto findNotice(Long notice_id);
}
