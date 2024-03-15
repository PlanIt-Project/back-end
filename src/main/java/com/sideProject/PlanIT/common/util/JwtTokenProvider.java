package com.sideProject.PlanIT.common.util;

import com.sideProject.PlanIT.config.JwtConfig;
import com.sideProject.PlanIT.domain.user.entity.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@AllArgsConstructor
public class JwtTokenProvider {
    private final JwtConfig jwtConfig;
    private final RedisUtil redisUtil;

    public final Long ACCESS_TOKEN_EXPIRE_LENGTH = 1000L * 20; // 10 sec
    public final Long REFRESH_TOKEN_EXPIRE_LENGTH = 1000L * 30; // 30 sec

    public String createAccessToken(Member member) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_LENGTH);
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, String.valueOf(jwtConfig.SECRET_KEY))
                .claim("memberId", member.getId())
                .claim("memberRole", member.getRole())
                .claim("type", "ACCESS")
                .setIssuedAt(now)
                .setExpiration(validity)
                .compact();
    }

    public String createRefreshToken(Member member) {

        redisUtil.deleteByValue(member.getId().toString());

        Date now = new Date();
        Date validity = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_LENGTH);
        String refreshToken =  Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, String.valueOf(jwtConfig.SECRET_KEY))
                .claim("type", "REFRESH")
                .setIssuedAt(now)
                .setExpiration(validity)
                .compact();
        redisUtil.setRefreshToken(refreshToken, member.getId());
        return refreshToken;
    }
}
