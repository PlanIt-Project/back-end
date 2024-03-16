package com.sideProject.PlanIT.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {
    @Value("${jwt.secret-key}")
    public String SECRET_KEY;
}
