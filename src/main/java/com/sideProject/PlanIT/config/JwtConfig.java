package com.sideProject.PlanIT.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${spring.jwt.secret-key}")
    public String SECRET_KEY;

    @Value("${spring.jwt.access-token-expire}")
    public Long ACCESS_TOKEN_EXPIRE;

    @Value("${spring.jwt.refresh-token-expire}")
    public Long REFRESH_TOKEN_EXPIRE;
}
