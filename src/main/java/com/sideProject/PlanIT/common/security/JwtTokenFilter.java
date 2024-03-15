package com.sideProject.PlanIT.common.security;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.common.util.JwtUtil;
import com.sideProject.PlanIT.common.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

@Configuration
@Slf4j
@AllArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwtToken = authorizationHeader.substring(7);
            try {
                if (jwtUtil.getTokenTypeFromToken(jwtToken).equals("ACCESS")) {

                    jwtUtil.validateToken(jwtToken);
                    log.info("유효한 토큰입니다. 검증 성공");

                    Long memberId = jwtUtil.getMemberIdFromToken(jwtToken);
                    String memberRole = jwtUtil.getMemberRoleFromToken(jwtToken);

                    // 권한 정보 설정, 인증 객체 생성
                    Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(memberRole));
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, null, authorities);

                    // 인증 객체 저장
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            } catch (Exception e) {
                request.setAttribute("exception", e.getMessage());
            }
        } else {
            log.info("토큰이 없습니다. 다시 로그인!");
        }

        // 필터 체인 계속 진행
        filterChain.doFilter(request, response);
    }
}