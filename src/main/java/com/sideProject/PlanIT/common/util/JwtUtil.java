package com.sideProject.PlanIT.common.util;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.config.JwtConfig;
import io.jsonwebtoken.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class JwtUtil {
    private final JwtConfig jwtConfig;
    private final RedisUtil redisUtil;

    public String getTokenTypeFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtConfig.SECRET_KEY).parseClaimsJws(token).getBody();
        return claims.get("type").toString();
    }

    public Long getMemberIdFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtConfig.SECRET_KEY).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.get("memberId").toString());
    }

    public String getMemberRoleFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtConfig.SECRET_KEY).parseClaimsJws(token).getBody();
        return claims.get("memberRole").toString();
    }

    public void validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtConfig.SECRET_KEY).parseClaimsJws(token);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    public void validateRefreshToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtConfig.SECRET_KEY).parseClaimsJws(token);
        } catch (Exception e) {
            redisUtil.deleteData(token);
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }
}
