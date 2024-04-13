package com.sideProject.PlanIT.domain.user.service;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.common.util.JwtTokenProvider;
import com.sideProject.PlanIT.common.util.JwtUtil;
import com.sideProject.PlanIT.common.util.RedisUtil;
import com.sideProject.PlanIT.domain.user.dto.member.response.JwtResponseDto;
import com.sideProject.PlanIT.domain.user.entity.Member;
import com.sideProject.PlanIT.domain.user.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    public JwtResponseDto refreshAccessToken(String authorizationHeader) {
        String refreshToken = authorizationHeader.substring(7);
        jwtUtil.validateRefreshToken(refreshToken);

        if (redisUtil.isExist(refreshToken)) {
            Long memberId = Long.parseLong(redisUtil.getData(refreshToken).toString());
            redisUtil.deleteData(refreshToken);
            Member member = memberRepository.findById(memberId).orElseThrow(() ->
                    new CustomException(ErrorCode.MEMBER_NOT_FOUND));
            return JwtResponseDto.builder()
                    .accessToken(jwtTokenProvider.createAccessToken(member))
                    .refreshToken(jwtTokenProvider.createRefreshToken(member))
                    .build();
        }
        throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
    }
}
